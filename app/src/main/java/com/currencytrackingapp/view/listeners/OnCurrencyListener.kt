package com.currencytrackingapp.view.listeners

interface OnCurrencyListener {

    fun onItemClicked(position: Int, currentBase: String, latestValue: String)

    fun onTypeListener(latestValue: String)
}