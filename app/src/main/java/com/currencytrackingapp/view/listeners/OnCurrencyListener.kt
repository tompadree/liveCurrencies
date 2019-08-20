package com.currencytrackingapp.view.listeners

import com.currencytrackingapp.data.models.RatesListItem

interface OnCurrencyListener {

    fun onItemClicked(position: Int, currentBase: String, latestValue: String)

    fun onTypeListener(latestValue: String)
}