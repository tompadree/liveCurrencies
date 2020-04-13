package com.currencytrackingapp.currencies

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.R
import com.currencytrackingapp.databinding.ActivityCurrenciesMainBinding
import com.currencytrackingapp.utils.helpers.observe
import com.currencytrackingapp.activities.BindingActivity
import kotlinx.android.synthetic.main.activity_currencies_main.*
import androidx.recyclerview.widget.SimpleItemAnimator


class CurrenciesActivity : BindingActivity<ActivityCurrenciesMainBinding>(),
    OnCurrencyListener {

    override val layoutId = R.layout.activity_currencies_main

//    private lateinit var viewModel: CurrenciesViewModel
    private lateinit var currentRatesAdapter: CurrentRatesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProviders.of(this).get(CurrenciesViewModel::class.java)
//        binding.viewModel = viewModel

        setView()
        observeViewModel()
    }


//    override fun onResume() {
//        super.onResume()
//        viewModel.resume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        viewModel.pause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        viewModel.destroy()
//    }

    private fun observeViewModel() {
//        observeError(viewModel.error)
//        observeLoading(viewModel.loading)
//
//        viewModel.error.observe(this){
//
//        }
//        viewModel.ratesListFetched.observe(this){
//            it?.let {
//                currentRatesAdapter.submitList(it)
//            }
//        }
    }

    override fun onItemClicked(position: Int, currentBase: String, latestValue: String) {
//        viewModel.currentBase.set(currentBase)
//        viewModel.currentValue.set(viewModel.roundOffDecimal(latestValue.toDouble()).toString())
//
//        val data = viewModel._currentList.value?.get(position)
//        viewModel._currentList.value?.remove(data)
//        viewModel._currentList.value?.addFirst(data)
//
//        currentRatesAdapter.submitList(viewModel._currentList.value!!)
//        currentRatesAdapter.notifyItemMoved(position, 0)

    }

    override fun onTypeListener(latestValue: String) {
//        viewModel.currentValue.set(viewModel.roundOffDecimal(latestValue.toDouble()).toString())
    }

    private fun setView() {

        showSnackbar(findViewById(android.R.id.content))

        currentRatesAdapter = CurrentRatesListAdapter(this, this)

        with(currenciesRv) {
            layoutManager = LinearLayoutManager(context)
            adapter = currentRatesAdapter
            setHasFixedSize(true)

            (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            currenciesRv.itemAnimator = null


            currentRatesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                }
            })

        }
    }

}