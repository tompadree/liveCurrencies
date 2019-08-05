package com.currencytrackingapp.view.listeners

import com.currencytrackingapp.data.models.RatesListItem

interface OnCurrencyListener {

    fun onItemClicked(position: Int)

    fun onTypeListener(latestValue: String)
}