package com.currencytrackingapp.view.activities.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.databinding.ActivityCurrenciesMainBinding
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.observe
import com.currencytrackingapp.view.activities.BindingActivity
import com.currencytrackingapp.view.adapters.CurrentRatesAdapter
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import com.currencytrackingapp.viewmodel.CurrenciesViewModel
import kotlinx.android.synthetic.main.activity_currencies_main.*
import org.koin.standalone.inject
import androidx.recyclerview.widget.SimpleItemAnimator
import com.currencytrackingapp.view.adapters.CurrentRatesListAdapter
import android.view.ViewTreeObserver
import android.graphics.Rect
import android.util.Log


class CurrenciesActivity : BindingActivity<ActivityCurrenciesMainBinding>(), OnCurrencyListener {

    override val layoutId = R.layout.activity_currencies_main

    private val appUtils: AppUtils by inject()

    private lateinit var viewModel: CurrenciesViewModel
    private lateinit var currentRatesAdapter: CurrentRatesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CurrenciesViewModel::class.java)
        binding.viewModel = viewModel

        setView()
        observeViewModel()
    }


    private fun observeViewModel() {

        observeError(viewModel.error)
        observeLoading(viewModel.loading)

        viewModel.error.observe(this){

        }
        viewModel.ratesListFetched.observe(this){
            it?.let {
                currentRatesAdapter.submitList(it)
            }
        }
    }

    override fun onItemClicked(position: Int, currentBase: String, latestValue: String) {
        viewModel.currentBase.set(currentBase)
        viewModel.currentValue.set(viewModel.roundOffDecimal(latestValue.toDouble()).toString())

        val data = viewModel._currentList.value?.get(position)
        viewModel._currentList.value?.remove(data)
        viewModel._currentList.value?.addFirst(data)

        currentRatesAdapter.submitList(viewModel._currentList.value!!)
        currentRatesAdapter.notifyItemMoved(position, 0)
//                viewModel.fetchRates(latestRates[0].name, latestRates[0].currentRate.toString(), latestRates)

    }

    override fun onTypeListener(latestValue: String) {
        viewModel.currentValue.set(viewModel.roundOffDecimal(latestValue.toDouble()).toString())
    }

    private fun setView() {

        currentRatesAdapter = CurrentRatesListAdapter(this, this)


        with(currenciesRv) {
            layoutManager = LinearLayoutManager(context)

            adapter = currentRatesAdapter

            currentRatesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                }
            })

            (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            currenciesRv.itemAnimator = null
        }
    }

}
