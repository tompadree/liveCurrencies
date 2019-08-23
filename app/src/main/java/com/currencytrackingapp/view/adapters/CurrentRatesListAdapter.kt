package com.currencytrackingapp.view.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.view.adapters.diffUtil.RatesDiffUtil
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.utils.helpers.CountryHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currencies.*


class CurrentRatesListAdapter(val activity: Activity, private val onCurrencyListener: OnCurrencyListener)
    : ListAdapter<RatesListItem, CurrentRateListViewHolder>(RatesDiffUtil()) {

    private var ratesListItem = RatesListItem()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRateListViewHolder {
        return CurrentRateListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currencies, parent, false), activity)
    }

    override fun getItemId(position: Int): Long = getItem(position).name.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int) {
//        if(position == 1 && previousZeroPosition())
//        {ratesListItem = getItem(1)
//            Log.e("ADAPTER", "position==1")}
//        else
            holder.bindView(position, getItem(position), onCurrencyListener)
    }

    override fun onViewRecycled(holder: CurrentRateListViewHolder) {
        holder.unbindView()
        super.onViewRecycled(holder)
    }

    private fun previousZeroPosition() = ratesListItem.name == getItem(1).name

}

class CurrentRateListViewHolder(view: View, val activity: Activity) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View? = view
    private var focusCleared = true
    private var lastPosition = 0
    private var keyboardShown = false
    private var wasTyping = false

    init {
        initKeyBoardListener()
    }


    fun bindView(position: Int, ratesListItem: RatesListItem, onCurrencyListener: OnCurrencyListener) {
//        Log.e("ADAPTER", "bindView")

        itemCurrenciesTvISO.text = ratesListItem.name

        itemCurrenciesTvName.text = activity.getString(CountryHelper.getNameForISO(ratesListItem.name))

        itemCurrenciesIvFlag.setImageResource(CountryHelper.getFlagForISO(ratesListItem.name))

        itemCurrenciesEtAmount.setText(ratesListItem.currentRate.toString())
//        itemCurrenciesEtAmount.setSelection(0)

        itemCurrenciesEtAmount.clearFocus()
//        itemCurrenciesEtAmount.setTag(1)

        itemCurrenciesLayout.setOnClickListener {

//                        itemCurrenciesEtAmount.clearFocus()
//            clearFocus(itemCurrenciesEtAmount, activity)
//            hideKeyboard(activity)
            onCurrencyListener.onItemClicked(position, ratesListItem.name, itemCurrenciesEtAmount.text.toString())
//            keyboardShown = false
            lastPosition = ratesListItem.currentRate.toString().length
        }

        if (position == 0) {
            setEdiTextListeners(itemCurrenciesEtAmount, activity, onCurrencyListener)
        }else {
//            itemCurrenciesEtAmount.clearFocus()
            itemCurrenciesEtAmount.isEnabled = false
        }

    }

    private fun setEdiTextListeners(itemCurrenciesEtAmount: EditText, activity: Activity, onCurrencyListener: OnCurrencyListener) {

        itemCurrenciesEtAmount.isEnabled = true

//        if(keyboardShown) {// && !itemCurrenciesEtAmount.isFocused) {
//            itemCurrenciesEtAmount.requestFocus()
//        }

//        if(wasTyping) {
//            itemCurrenciesEtAmount.performClick()
//            wasTyping = false
//        }

        itemCurrenciesEtAmount.setOnClickListener {
            focusCleared = false
            it.requestFocus()}

        itemCurrenciesEtAmount.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            var test = 0
            Log.e("ADAPTER", "before OnFocusChaner1keyboarshown " + keyboardShown.toString())

            if (!focusCleared && keyboardShown) { //K && isShowKeyboard(activity)){ //} && p0.tag == 1) {
//                if (!p0.isFocused && !p0.hasFocus())
                    itemCurrenciesEtAmount.requestFocus()
                itemCurrenciesEtAmount.setSelection(lastPosition)
                Log.e("ADAPTER", "OnFocusChaner1 " + (++test).toString())
//                focusCleared = true
                p0.tag = 0
            }
            else {
//                itemCurrenciesEtAmount.clearFocus()
                focusCleared = false
            }
            Log.e("ADAPTER", "OnFocusChangeListener " + (++test).toString())
        }
        itemCurrenciesEtAmount.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus(itemCurrenciesEtAmount)
//                keyboardShown = false
            }
            false
        }

//            itemCurrenciesEtAmount.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
////                if (keyCode == KeyEvent.KEYCODE_BACK) {
////                    clearFocus(itemCurrenciesEtAmount)
////                    return@OnKeyListener true
////                }
//                false
//            })
//            if (isInit) {

        itemCurrenciesEtAmount.addTextChangedListener(object : TextWatcher {

            var beforeCount = 0

            override fun afterTextChanged(p0: Editable) {
                if (beforeCount != p0.length)
                    lastPosition = itemCurrenciesEtAmount.selectionStart

                itemCurrenciesEtAmount.requestFocus()
            }

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                beforeCount = p0.length
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                if(beforeCount!= count)
                    onCurrencyListener.onTypeListener(p0.toString())
            }
        })

    }

    fun updateRates(position: Int, ratesListItem: RatesListItem, onCurrencyListener: OnCurrencyListener) {
        Log.e("ADAPTER", "updateRates")
        itemCurrenciesEtAmount.clearFocus()
        itemCurrenciesEtAmount.setText(ratesListItem.currentRate.toString())
        itemCurrenciesEtAmount.clearFocus()

    }


    private fun clearFocus(itemCurrenciesEtAmount: EditText) {
        focusCleared = true
        itemCurrenciesEtAmount.clearFocus()
        hideKeyboard()
        wasTyping = false
    }

    private fun showKeyboard() {
        val view = activity.currentFocus

        val imm = (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.showSoftInput(view, InputMethod.SHOW_FORCED)

        if(imm.isAcceptingText)
            itemCurrenciesEtAmount.requestFocus()

    }

    private fun isShowKeyboard() : Boolean {
        val view = activity.currentFocus

        val imm = (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.showSoftInput(view, InputMethod.SHOW_FORCED)

        return imm.isAcceptingText
    }

    fun hideKeyboard() {
        val view = activity.currentFocus

        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view?.windowToken ?: return, 0)
    }

    fun unbindView() {}

    private fun initKeyBoardListener() {
        // Threshold for minimal keyboard height.
        val MIN_KEYBOARD_HEIGHT_PX = 150
        // Top-level window decor view.
        val decorView = activity.window.decorView
        // Регистрируем глобальный слушатель. Register global layout listener.
        decorView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            // Retrieve visible rectangle inside window.
            private val windowVisibleDisplayFrame = Rect()
            private var lastVisibleDecorViewHeight: Int = 0

            override fun onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        Log.e("Pasha", "SHOW")
                        keyboardShown = true
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        Log.e("Pasha", "HIDE")
                        keyboardShown = false
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight
            }
        })
    }
}
