package com.currencytrackingapp.view.adapters.diffUtil

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.currencytrackingapp.data.models.RatesListItem

class RatesDiffUtil : DiffUtil.ItemCallback<RatesListItem>() {

    override fun areItemsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
//        Log.e("ADAPTER", "areItemsTheSame = " + (oldItem.name == newItem.name
//                && oldItem.currentRate == newItem.currentRate).toString())
        return oldItem.name == newItem.name
                && oldItem.currentRate == newItem.currentRate
    }

    override fun areContentsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
//        Log.e("ADAPTER", "areContentsTheSame = " + (oldItem.name == newItem.name
//                && oldItem.currentRate == newItem.currentRate).toString())
        return oldItem.name == newItem.name
                && oldItem.currentRate == newItem.currentRate
    }
}