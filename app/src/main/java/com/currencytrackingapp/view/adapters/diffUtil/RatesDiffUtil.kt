package com.currencytrackingapp.view.adapters.diffUtil

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.currencytrackingapp.data.models.RatesListItem

class RatesDiffUtil : DiffUtil.ItemCallback<RatesListItem>() {

    override fun areItemsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
                return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
        return oldItem.currentRate == newItem.currentRate
    }
}