package com.currencytrackingapp.currencies

import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.databinding.RatesItemBinding
import com.currencytrackingapp.currencies.RatesListAdapter.RatesViewHolder
import com.currencytrackingapp.utils.helpers.CountryHelper
import timber.log.Timber

/**
 * @author Tomislav Curis
 */
class RatesListAdapter(private val currenciesViewModel: CurrenciesViewModel)
    : ListAdapter<RatesListItem, RatesViewHolder>(RatesDiffUtil()) {

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(currenciesViewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        return RatesViewHolder.from(parent)
    }

    class RatesViewHolder private constructor(val binding: RatesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CurrenciesViewModel, item: RatesListItem) {

            binding.viewModel = viewModel
            binding.ratesItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RatesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RatesItemBinding.inflate(layoutInflater, parent, false)

                return RatesViewHolder(binding)
            }
        }
    }

}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
//class RatesDiffCallback : DiffUtil.ItemCallback<Task>() {
//    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
//        return oldItem == newItem
//    }
//}

class RatesDiffUtil : DiffUtil.ItemCallback<RatesListItem>() {

    override fun areItemsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RatesListItem, newItem: RatesListItem): Boolean {
        return oldItem.currentRate == newItem.currentRate
    }
}