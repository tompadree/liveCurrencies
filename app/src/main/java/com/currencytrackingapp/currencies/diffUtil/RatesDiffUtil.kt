package com.currencytrackingapp.currencies.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.currencytrackingapp.data.models.RatesListItem

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class RatesDiffUtil : DiffUtil.ItemCallback<RatesListItem>() {

    override fun areItemsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
                return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
        return oldItem.currentRate == newItem.currentRate
    }
}