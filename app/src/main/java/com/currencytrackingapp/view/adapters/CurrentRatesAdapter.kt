package com.currencytrackingapp.view.adapters

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.utils.helpers.CountryHelper
import com.currencytrackingapp.view.adapters.diffUtil.RatesDiffUtil
import com.currencytrackingapp.view.adapters.viewholders.CurrentRateListViewHolder
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currencies.*
import java.util.*
import android.text.method.TextKeyListener.clear
import androidx.recyclerview.widget.DiffUtil


class CurrentRatesAdapter (
    val activity: Activity,
    private val onCurrencyListener: OnCurrencyListener
) : RecyclerView.Adapter<CurrentRateListViewHolder>() {

    private var isInit = true

    init {
        setHasStableIds(true)
    }

    private val currentRatesDiffer = AsyncListDiffer<RatesListItem>(this, RatesDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRateListViewHolder {

        Log.e("ADAPTER", "getItemCount")
        return  CurrentRateListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currencies, parent, false))

    }

    override fun getItemCount(): Int {
//        Log.e("ADAPTER", "getItemCount")
        return currentRatesDiffer.currentList.size
    }

    override fun getItemId(position: Int): Long = currentRatesDiffer.currentList[position].name.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int) {
        Log.e("ADAPTER", "onBindViewHolder")
//        if(position == 0) {
//            holder.bindView(position, activity, currentRatesDiffer.currentList[position], onCurrencyListener)
//            if(isInit) isInit = false
//        } else if(position != 0)
            holder.bindView(position, activity, currentRatesDiffer.currentList[position], onCurrencyListener)
    }

    override fun onViewRecycled(holder: CurrentRateListViewHolder) {
        Log.e("ADAPTER", "onViewRecycled")
        holder.unbindView()
        super.onViewRecycled(holder)
    }

    fun setData(rateList: LinkedList<RatesListItem>) {
        Log.e("ADAPTER", "setData")
        val newList: LinkedList<RatesListItem> = LinkedList()
        newList.addAll(rateList)
//        currentRatesDiffer.submitList(null)
//        currentRatesDif fer.submitList(newList.toList())

        currentRatesDiffer.submitList(newList)


    }

}