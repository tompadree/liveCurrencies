package com.currencytrackingapp.currencies

interface OnCurrencyListener {

    fun onItemClicked(position: Int, currentBase: String, latestValue: String)

    fun onTypeListener(latestValue: String)
}