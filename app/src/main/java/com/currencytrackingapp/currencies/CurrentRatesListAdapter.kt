package com.currencytrackingapp.currencies

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.currencies.diffUtil.RatesDiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.currencytrackingapp.currencies.viewholders.CurrentRateListViewHolder

class CurrentRatesListAdapter(val activity: Activity, private val onCurrencyListener: OnCurrencyListener)
    : ListAdapter<RatesListItem, CurrentRateListViewHolder>(RatesDiffUtil()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRateListViewHolder {
        return CurrentRateListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currencies, parent, false), activity)
    }

    override fun getItemId(position: Int): Long = getItem(position).name.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int) {
            holder.bindView(position, getItem(position), onCurrencyListener)
    }

    override fun onViewRecycled(holder: CurrentRateListViewHolder) {
        holder.unbindView()
        super.onViewRecycled(holder)
    }

}
