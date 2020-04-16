package com.currencytrackingapp.currencies

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.databinding.RatesItemBinding
import com.currencytrackingapp.currencies.diffUtil.RatesDiffUtil
import com.currencytrackingapp.currencies.viewholders.CurrentRateListViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rates_item.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.currencytrackingapp.generated.callback.OnClickListener
import kotlinx.android.synthetic.main.rates_item.itemCurrenciesEtAmount
import kotlinx.android.synthetic.main.rates_item.view.*


/**
 * @author Tomislav Curis
 */
class RatesListAdapter(private val currenciesViewModel: CurrenciesViewModel, private val activity: Activity)
    : ListAdapter<RatesListItem, RatesViewHolder>(RatesDiffUtil()) { //CurrentRateListViewHolde>(RatesDiffUtil()) {

//    init {
//        setHasStableIds(true)
//    }
//
    var firstItemCurrentState = false

    var lastBase = ""

    override fun getItemId(position: Int): Long = getItem(position).name.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {

        // Don't refresh first item if only new rates came
        if(position == 0 && lastBase == getItem(position).name)
            return
        else if(position == 0)
            lastBase = getItem(position).name

        holder.bind(currenciesViewModel, getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        return RatesViewHolder.from(parent)
    }

    // close keyboard if first item is not visible
    override fun onViewDetachedFromWindow(holder: RatesViewHolder) {
        holder.binding.itemCurrenciesEtAmount.clearFocus()
        super.onViewDetachedFromWindow(holder)
        holder.binding.itemCurrenciesEtAmount.clearFocus()
    }



//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRateListViewHolder {
//        return CurrentRateListViewHolder.from(parent, activity)
//    }
//
//    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int) {
//        holder.bindView(position, getItem(position), currenciesViewModel)
//    }



}

class RatesViewHolder private constructor(val binding: RatesItemBinding) : RecyclerView.ViewHolder(binding.root), LayoutContainer {

    override val containerView = binding.root

    companion object {
        fun from(parent: ViewGroup): RatesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RatesItemBinding.inflate(layoutInflater, parent, false)

            return RatesViewHolder(binding)
        }
    }

    fun onUnbind(){
        binding.itemCurrenciesEtAmount.clearFocus()
    }

    fun bind(viewModel: CurrenciesViewModel, item: RatesListItem, position: Int) {

        binding.viewModel = viewModel
        binding.ratesItem = item
        binding.executePendingBindings()

        itemCurrenciesEtAmount.isEnabled = false
        itemCurrenciesEtAmount.clearFocus()

        if(position == 0) { // && !itemCurrenciesEtAmount.isFocused) { // && !itemCurrenciesEtAmount.isEnabled) {
            itemCurrenciesEtAmount.isEnabled = true

            itemCurrenciesEtAmount.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext

                    itemCurrenciesEtAmount.clearFocus()
                }
                false
            }
        }else {

        }
    }
}

class MyEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    var pressedFocus = false

    fun onClickListener() {
        pressedFocus = !pressedFocus
    }

    override fun isInEditMode(): Boolean {
        return super.isInEditMode()
    }


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {

        clearFocus()
        // Event is handled therefore return true
        return true
    }

    // close keyboard if first item is not visible
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {

            if(pressedFocus) {

                pressedFocus = false
                return
            }
            super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if(!focused) {
            // Close keyboard
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken ?: return, 0)
//            clearFocus()
//            pressedFocus = false

        }

//        if(!pressedFocus && focused){
//////            pressedFocus = true
//            clearFocus()
//        }
    }

}