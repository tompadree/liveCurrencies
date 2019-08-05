package com.currencytrackingapp.view.activities.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.databinding.ActivityCurrenciesMainBinding
import com.currencytrackingapp.utils.observe
import com.currencytrackingapp.view.activities.BindingActivity
import com.currencytrackingapp.view.adapters.CurrentRatesAdapter
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import com.currencytrackingapp.viewmodel.CurrenciesViewModel
import kotlinx.android.synthetic.main.activity_currencies_main.*
import java.util.*

class CurrenciesActivity : BindingActivity<ActivityCurrenciesMainBinding>() {

    override val layoutId = R.layout.activity_currencies_main

    private lateinit var viewModel: CurrenciesViewModel
    private lateinit var currentRatesAdapter: CurrentRatesAdapter
    private var latestRates: LinkedList<RatesListItem> = LinkedList()
    private var latestRateCheck = "100.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CurrenciesViewModel::class.java)
        binding.viewModel = viewModel

        setView()
        observeViewModel()
        fetchRatesInit()

    }

    private fun fetchRatesInit() = viewModel.fetchRates(getString(R.string.eur_), "100.0", latestRates)

    private fun observeViewModel() {

        observeError(viewModel.error)
        observeLoading(viewModel.loading)

        viewModel.error.observe(this){

        }



        viewModel.ratesListFetched.observe(this){
            it?.let {
                latestRates = it
                setDataFromViewModel() }
        }
    }

    private fun setView() {

        currentRatesAdapter = CurrentRatesAdapter(this, object : OnCurrencyListener {

            override fun onItemClicked(position: Int) {
                val data = latestRates[position]
                latestRates.remove(data)
                latestRates.addFirst(data)

                currentRatesAdapter.notifyItemMoved(position, 0)
                currentRatesAdapter.setData(latestRates)
//                viewModel.fetchRates(latestRates[0].name, latestRates[0].currentRate.toString(), latestRates)

            }

            override fun onTypeListener(latestValue: String) {
                latestRates[0].currentRate = viewModel.roundOffDecimal(latestValue.toDouble())

                Log.e("RATE", latestRateCheck + " != " + latestValue)

//                if( latestRateCheck !=  latestValue) {

                if( viewModel.roundOffDecimal(latestRateCheck.toDouble()) !=  viewModel.roundOffDecimal(latestValue.toDouble())) {
                    viewModel.fetchRates(latestRates[0].name, latestValue, latestRates)
                    latestRateCheck = latestValue
                }
            }
        })

        currentRatesAdapter.setHasStableIds(true)

        with(currenciesRv) {
            layoutManager = LinearLayoutManager(context)
            adapter = currentRatesAdapter


            currentRatesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                }
            })


//            setHasFixedSize(true)
        }
    }

    private fun setDataFromViewModel(){ currentRatesAdapter.setData(latestRates) }
}
