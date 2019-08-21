package com.currencytrackingapp.view.adapters.viewholders

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.helpers.CountryHelper
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currencies.*


class CurrentRateListViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View? = view
    private var focusCleared = true
    private var lastPosition = 0
    private var remainFocus = false
    private var keyboardShown = false

    fun bindView(position: Int, activity: Activity, ratesListItem: RatesListItem, onCurrencyListener: OnCurrencyListener) {
//        Log.e("ADAPTER", "bindView")

        itemCurrenciesTvISO.text = ratesListItem.name

        itemCurrenciesTvName.text = activity.getString(CountryHelper.getNameForISO(ratesListItem.name))

        itemCurrenciesIvFlag.setImageResource(CountryHelper.getFlagForISO(ratesListItem.name))

        itemCurrenciesEtAmount.setText(ratesListItem.currentRate.toString())
        itemCurrenciesEtAmount.clearFocus()

        itemCurrenciesLayout.setOnClickListener {
//            if(!itemCurrenciesEtAmount.isFocused)
//                hideKeyboard(activity)
//
//            itemCurrenciesEtAmount.clearFocus()




            onCurrencyListener.onItemClicked(position, ratesListItem.name, itemCurrenciesEtAmount.text.toString())
//            showKeyboard(activity)
//            if(itemCurrenciesEtAmount.isFocused || itemCurrenciesEtAmount.hasFocus())
//                showKeyboard(activity)
//            else
//                hideKeyboard(activity)
            itemCurrenciesEtAmount.clearFocus()
//            clearFocus(itemCurrenciesEtAmount, activity)
            focusCleared = true
            lastPosition = ratesListItem.currentRate.toString().length
        }

        if (position == 0) {
            setEdiTextListeners(itemCurrenciesEtAmount, activity, onCurrencyListener)
        }else {
            itemCurrenciesEtAmount.isEnabled = false
        }


    }

    fun updateRates(position: Int, activity: Activity, ratesListItem: RatesListItem, onCurrencyListener: OnCurrencyListener) {
        Log.e("ADAPTER", "updateRates")
        itemCurrenciesEtAmount.clearFocus()
        itemCurrenciesEtAmount.setText(ratesListItem.currentRate.toString())
        itemCurrenciesEtAmount.clearFocus()

    }


    private fun clearFocus(itemCurrenciesEtAmount: EditText, activity: Activity) {
        focusCleared = true
        itemCurrenciesEtAmount.clearFocus()
        hideKeyboard(activity)
    }

    private fun showKeyboard(activity: Activity) {
        val view = activity.currentFocus

        val imm = (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.showSoftInput(view, InputMethod.SHOW_FORCED)

        if(imm.isAcceptingText)
            itemCurrenciesEtAmount.requestFocus()

        keyboardShown = true

    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus

        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view?.windowToken ?: return, 0)

        keyboardShown = false
    }

    private fun setEdiTextListeners(itemCurrenciesEtAmount: EditText, activity: Activity, onCurrencyListener: OnCurrencyListener) {

        itemCurrenciesEtAmount.isEnabled = true

//        if(!itemCurrenciesEtAmount.hasFocus() || !itemCurrenciesEtAmount.isFocused)
//        if(keyboardShown) {
//            itemCurrenciesEtAmount.requestFocus()
//            showKeyboard(activity)
//        }

        itemCurrenciesEtAmount.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (!focusCleared) {
//                if(!itemCurrenciesEtAmount.isFocused)
//                showKeyboard(activity)
                itemCurrenciesEtAmount.requestFocus()
                itemCurrenciesEtAmount.setSelection(lastPosition)
                Log.e("ADAPTER", "OnFocusChaner1")
            }
            else
                focusCleared = false
            Log.e("ADAPTER", "OnFocusChangeListener")
        }
        itemCurrenciesEtAmount.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus(itemCurrenciesEtAmount, activity)
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

    fun unbindView() {}
}
