package com.currencytrackingapp.view.adapters.viewholders

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.utils.helpers.CountryHelper
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currencies.*


class CurrentRateListViewHolder(view: View, val activity: Activity) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View? = view
    private var focusCleared = true
    private var lastPosition = 0
    private var keyboardShown = false

    fun bindView(position: Int, ratesListItem: RatesListItem, onCurrencyListener: OnCurrencyListener) {

        initKeyBoardListener(itemCurrenciesEtAmount)
        itemCurrenciesTvISO.text = ratesListItem.name

        itemCurrenciesTvName.text = activity.getString(CountryHelper.getNameForISO(ratesListItem.name))

        itemCurrenciesIvFlag.setImageResource(CountryHelper.getFlagForISO(ratesListItem.name))

        itemCurrenciesEtAmount.setText(ratesListItem.currentRate.toString())
        itemCurrenciesEtAmount.clearFocus()

        itemCurrenciesLayout.setOnClickListener {

            onCurrencyListener.onItemClicked(position, ratesListItem.name, itemCurrenciesEtAmount.text.toString())
            lastPosition = ratesListItem.currentRate.toString().length

        }

        if (position == 0) {
            setEdiTextListeners(itemCurrenciesEtAmount, onCurrencyListener)
        }else {
            itemCurrenciesEtAmount.isEnabled = false
        }

    }

    private fun setEdiTextListeners(itemCurrenciesEtAmount: EditText, onCurrencyListener: OnCurrencyListener) {

        itemCurrenciesEtAmount.isEnabled = true

        itemCurrenciesEtAmount.setOnClickListener {
            focusCleared = false
            it.requestFocus()}

        itemCurrenciesEtAmount.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (!focusCleared && keyboardShown) {
                itemCurrenciesEtAmount.requestFocus()
                itemCurrenciesEtAmount.setSelection(lastPosition)
            }
            else {
                focusCleared = false
            }
        }
        itemCurrenciesEtAmount.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus(itemCurrenciesEtAmount)
            }
            false
        }

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

    private fun clearFocus(itemCurrenciesEtAmount: EditText) {
        focusCleared = true
        itemCurrenciesEtAmount.clearFocus()
    }

    private fun showKeyboard() {
        val view = activity.currentFocus

        val imm = (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.showSoftInput(view, InputMethod.SHOW_FORCED)

        if(imm.isAcceptingText)
            itemCurrenciesEtAmount.requestFocus()

    }

    fun hideKeyboard() {
        val view = activity.currentFocus

        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view?.windowToken ?: return, 0)
    }

    fun unbindView() {}

    private fun initKeyBoardListener(itemCurrenciesEtAmount: EditText) {
        // Threshold for minimal keyboard height.
        val MIN_KEYBOARD_HEIGHT_PX = 150
        // Top-level window decor view.
        val decorView = activity.window.decorView

        decorView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            // Retrieve visible rectangle inside window.
            private val windowVisibleDisplayFrame = Rect()
            private var lastVisibleDecorViewHeight: Int = 0

            override fun onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        keyboardShown = true
                        itemCurrenciesEtAmount.requestFocus()
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        keyboardShown = false
                        itemCurrenciesEtAmount.clearFocus()
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight
            }
        })
    }
}
