package com.currencytrackingapp.currencies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SimpleItemAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.currencytrackingapp.R
import com.currencytrackingapp.databinding.FragmentCurrenciesBinding
import com.currencytrackingapp.base.BindingFragment
import kotlinx.android.synthetic.main.fragment_currencies.*
import com.currencytrackingapp.utils.helpers.observe

class CurrenciesFragment : BindingFragment<FragmentCurrenciesBinding>() {

    private var emptyShimmerCheck = true

    override val layoutId = R.layout.fragment_currencies

    private val viewModel: CurrenciesViewModel by viewModel()

    private lateinit var currentRatesAdapter: RatesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


        shimmerViewContainer.startShimmer()
        setupObservers()
        setupRv()
        viewModel.initVM()
    }


    private fun setupObservers(){

        observeError(viewModel.error)

        viewModel.empty.observe(this) {

            if(it == true) {
                emptyShimmerCheck = true
                shimmerViewContainer.startShimmer()
            } else if(it == false && emptyShimmerCheck) {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                emptyShimmerCheck = false
            }
        }

        viewModel.isDataLoadingError.observe(this) {
            it?.let {
                swipeRefreshLayout.isEnabled = it
                swipeRefreshLayout.isRefreshing = it
            }
        }
    }

    private fun setupRv() {
        currentRatesAdapter = RatesListAdapter(viewModel)

        with(currenciesRv) {
            layoutManager = LinearLayoutManager(context)
            adapter = currentRatesAdapter
            (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            // width and height don't change
            setHasFixedSize(true)

            // Set the number of offscreen views to retain before adding them
            // to the potentially shared recycled view pool
            setItemViewCacheSize(32)


            // Scroll to first item after change
            currentRatesAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                }
            })
        }
    }
}
