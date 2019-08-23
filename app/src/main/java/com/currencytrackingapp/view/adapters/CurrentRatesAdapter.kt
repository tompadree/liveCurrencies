package com.currencytrackingapp.view.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.view.adapters.diffUtil.RatesDiffUtil
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import java.util.*
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
//    private var curList = LinkedList<RatesListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRateListViewHolder {

        Log.e("ADAPTER", "getItemCount")
        return CurrentRateListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_currencies,
                parent,
                false
            ), activity
        )

    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        Log.e("ADAPTER", "getItemCount")
        return currentRatesDiffer.currentList.size
    }

    override fun getItemId(position: Int): Long = currentRatesDiffer.currentList[position].name.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int) {
        Log.e("ADAPTER", "onBindViewHolder")
//        if(position == 0) {
//            holder.bindView(position, activity, currentRatesDiffer.currentList[position], onCurrencyListener)
//            if(isInit) isInit = false
//        } else if(position != 0)
//        holder.bindView(position, activity, curList[position], onCurrencyListener)
    }

//    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int, payloads: MutableList<Any>) {
//        if (payloads.isEmpty()) {
//            super.onBindViewHolder(holder, position, payloads)
//        } else {
//            val o = payloads[0] as Bundle
//            for (key in o.keySet()) {
//                if (key == "price") {
//                    holder.bindView(position, activity, curList[position], onCurrencyListener)
//
////                    holder.mName.setText(data.get(position).name)
////                    holder.mPrice.setText(data.get(position).price + " USD")
////                    holder.mPrice.setTextColor(Color.GREEN)
//                }
//            }
//        }
//    }

//    override fun onBindViewHolder(holder: CurrentRateListViewHolder, position: Int, payloads: List<RatesListItem>) {
//        Log.e("ADAPTER", "onBindViewHolder")
////        if(position == 0) {
////            holder.bindView(position, activity, currentRatesDiffer.currentList[position], onCurrencyListener)
////            if(isInit) isInit = false
////        } else if(position != 0)
////        holder.bindView(position, activity, curList[position], onCurrencyListener)
//
//        val o = payloads.get(0) as Bundle
//        for (key in o.keySet()) {
//            if (key == "price") {
////                holder.mName.setText(data.get(position).name)
////                holder.mPrice.setText(data.get(position).price + " USD")
////                holder.mPrice.setTextColor(Color.GREEN)
//            }
//        }
//    }

    override fun onViewRecycled(holder: CurrentRateListViewHolder) {
        Log.e("ADAPTER", "onViewRecycled")
        holder.unbindView()
        super.onViewRecycled(holder)
    }

    fun setData(rateList: LinkedList<RatesListItem>) {
        Log.e("ADAPTER", "setData")

//        if(currentRatesDiffer.currentList.size > 0)
//            return

//        val newList: LinkedList<RatesListItem> = LinkedList()
//        newList.addAll(rateList)

        currentRatesDiffer.submitList(rateList)

//        if (curList.size == 0) {
//            curList.addAll((rateList))
////            currentRatesDiffer.submitList(newList)
//        }
//        else
//            curList.addAll(newList)
////        else
//        updateListItems(newList)


    }

//    fun updateListItems(rates: List<RatesListItem>) {
//        val diffCallback = DiffUtilRates(curList, rates)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        curList.clear()
//        curList.addAll(rates)
//        diffResult.dispatchUpdatesTo(this)
//    }

}

class DiffUtilRates(private val oldItems: List<RatesListItem>, private val newItems: List<RatesListItem>)
    : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].currentRate == newItems[newItemPosition].currentRate

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return super.getChangePayload(oldItemPosition, newItemPosition)

//        return RatesListItem(
//            oldItem,
//            newItem)
    }
}
