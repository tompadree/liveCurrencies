package com.currencytrackingapp.view.adapters.viewholders

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.utils.helpers.CountryHelper
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currencies.*


class CurrentRateListViewHolder (view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View? = view

    fun bindView(isInit: Boolean, position: Int, context: Context, ratesListItem: RatesListItem, onCurrencyListener: OnCurrencyListener) {
        Log.e("ADAPTER", "bindView")

        itemCurrenciesLayout.setOnClickListener { onCurrencyListener.onItemClicked(position) }

        itemCurrenciesTvISO.text = ratesListItem.name

        itemCurrenciesTvName.text = context.getString(CountryHelper.getNameForISO(ratesListItem.name))

        itemCurrenciesIvFlag.setImageResource(CountryHelper.getFlagForISO(ratesListItem.name))

        itemCurrenciesEtAmount.setText(ratesListItem.currentRate.toString())

        if (position == 0) {
            Log.e("ADAPTER", "position == 0")
//            itemCurrenciesEtAmount.isEnabled = true
//            itemCurrenciesEtAmount.requestFocus()
//            if (isInit) {
                itemCurrenciesEtAmount.isEnabled = true
                Log.e("ADAPTER", "isInit == " + isInit.toString())
                itemCurrenciesEtAmount.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(p0: Editable?) {}

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                        Log.e("ADAPTER", p0.toString())
                        onCurrencyListener.onTypeListener(p0.toString())
                    }
                })
//            }
    } else
            itemCurrenciesEtAmount.isEnabled = false


    }

    fun unbindView(){}
}
