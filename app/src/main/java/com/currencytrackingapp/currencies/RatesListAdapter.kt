package com.currencytrackingapp.currencies

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.databinding.RatesItemBinding
import com.currencytrackingapp.currencies.diffUtil.RatesDiffUtil
import kotlinx.android.extensions.LayoutContainer
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.rates_item.itemCurrenciesEtAmount


/**
 * @author Tomislav Curis
 */
class RatesListAdapter(private val currenciesViewModel: CurrenciesViewModel)
    : ListAdapter<RatesListItem, RatesViewHolder>(RatesDiffUtil()) { //CurrentRateListViewHolde>(RatesDiffUtil()) {

    init {
        Log.e("TEST", "ADAPTER IS INIT")
//        setHasStableIds(true)
    }

    var lastBase = ""

    override fun getItemId(position: Int): Long = getItem(position).name.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {

        // Don't refresh first item if only new rates came
        if(position == 0 && lastBase == getItem(position).name)
            return
        else if(position == 0) {
            lastBase = getItem(position).name
        }

        holder.bind(currenciesViewModel, getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        return RatesViewHolder.from(parent)
    }

    // close keyboard if first item is not visible
//    override fun onViewDetachedFromWindow(holder: RatesViewHolder) {
//        super.onViewDetachedFromWindow(holder)
//        holder.onUnbind()
//    }

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

        if(position == 0) {
            itemCurrenciesEtAmount.isEnabled = true

            itemCurrenciesEtAmount.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext
                    itemCurrenciesEtAmount.clearFocus()
                }
                false
            }
            itemCurrenciesEtAmount.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
                override fun onViewDetachedFromWindow(v: View?) {}

                override fun onViewAttachedToWindow(v: View?) {
                    // Workaround to remove first focus
                    itemCurrenciesEtAmount.requestFocus()
                    itemCurrenciesEtAmount.clearFocus()
                }
            })

        }
    }
}

class MyEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {

        clearFocus()
        // Event is handled therefore return true
        return true
    }

    // close keyboard if first item is not visible
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if(!focused) {
            // Close keyboard
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken ?: return, 0)
        }
    }
}