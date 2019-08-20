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
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.observe
import com.currencytrackingapp.view.activities.BindingActivity
import com.currencytrackingapp.view.adapters.CurrentRatesAdapter
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import com.currencytrackingapp.viewmodel.CurrenciesViewModel
import kotlinx.android.synthetic.main.activity_currencies_main.*
import org.koin.standalone.inject
import java.util.*
import android.os.Handler
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.SimpleItemAnimator
import com.currencytrackingapp.view.adapters.CurrentRatesListAdapter


class CurrenciesActivity : BindingActivity<ActivityCurrenciesMainBinding>() {

    override val layoutId = R.layout.activity_currencies_main

    private val appUtils: AppUtils by inject()

    private lateinit var viewModel: CurrenciesViewModel
    private lateinit var currentRatesAdapter: CurrentRatesListAdapter
//    private var latestRates: LinkedList<RatesListItem> = LinkedList()
    private var latestRateCheck = "100.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CurrenciesViewModel::class.java)
        binding.viewModel = viewModel

        setView()
        observeViewModel()
//        fetchRatesInit()

    }

//    private fun fetchRatesInit() = viewModel.fetchRates(getString(R.string.eur_), "100.0", latestRates)

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

    private fun setView() {

        currentRatesAdapter = CurrentRatesListAdapter(this, object : OnCurrencyListener {

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
//
//                Log.e("RATE", latestRateCheck + " != " + latestValue)
//
////                if( latestRateCheck !=  latestValue) {
//
//                if( viewModel.roundOffDecimal(latestRateCheck.toDouble()) !=  viewModel.roundOffDecimal(latestValue.toDouble())) {
//                    viewModel.fetchRates(latestRates[0].name, latestValue, latestRates)
//                    latestRateCheck = latestValue
//                }
            }
        })

        with(currenciesRv) {
            layoutManager = LinearLayoutManager(context) // {

//                override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
//                    var nScroll = 0
//                    // Do not let auto scroll
//                    if (currenciesRv.getScrollState() !== RecyclerView.SCROLL_STATE_SETTLING) {
//                        nScroll = super.scrollHorizontallyBy(dx, recycler, state)
//                    }
//
//                    return 0
//                }
//
//                override fun requestChildRectangleOnScreen(parent: RecyclerView, child: View, rect: Rect, immediate: Boolean): Boolean {
//                    return false
//                }
//                override fun requestChildRectangleOnScreen(parent: RecyclerView, child: View, rect: Rect, immediate: Boolean, focusedChildVisible: Boolean): Boolean {
//                        return false
//                }
//            }
            adapter = currentRatesAdapter

            currentRatesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                }
            })

            (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//            itemAnimator = null
        }
    }

}
