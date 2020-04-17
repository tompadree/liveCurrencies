package com.currencytrackingapp.currencies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SimpleItemAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.currencytrackingapp.R
import com.currencytrackingapp.databinding.FragmentCurrenciesBinding
import com.currencytrackingapp.base.BindingFragment
import com.currencytrackingapp.base.NavigationActivity
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.fragment_currencies.*
import timber.log.Timber
import com.currencytrackingapp.utils.helpers.observe
import com.currencytrackingapp.utils.network.ConnectivityChecker
import com.currencytrackingapp.utils.network.InternetConnectionManager
import org.koin.android.ext.android.inject

class CurrenciesFragment : BindingFragment<FragmentCurrenciesBinding>() {


    override val layoutId = R.layout.fragment_currencies

    private val viewModel: CurrenciesViewModel by viewModel()

    private val connectivityChecker: ConnectivityChecker by inject()
    private val internetConnectionManager: InternetConnectionManager by inject()

    private lateinit var currentRatesAdapter: RatesListAdapter

//    private lateinit var viewDataBinding: CurrenciesFrag
//    {
//        //        TasksViewModelFactory(DefaultTasksRepository.getRepository(requireActivity().application))
//        TasksViewModelFactory((requireActivity().applicationContext as TodoApplication).tasksRepository)
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_currencies, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


        shimmerViewContainer.startShimmer()
        setupObservers()
        setupRv()

    }

    // Better solution than flag binded in layout
    var emptyCheck = true
    private fun setupObservers(){
        viewModel.empty.observe(this) {

            if(it == true) {
                emptyCheck = true
                shimmerViewContainer.startShimmer()
            } else if(it == false && emptyCheck) {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                emptyCheck = false
            }
        }



//        connectivityChecker.apply {
//                binding.lifecycleOwner?.let {
//                    lifecycle.addObserver(this)
//                    connectedStatus.observe(it, Observer { connected ->
//                        viewModel._forceUpdate.value = connected
//                    })
//                }
//        }
    }

    private fun setupRv() {
        if (viewModel != null) {
            currentRatesAdapter = RatesListAdapter(viewModel, activity as NavigationActivity)

            with(currenciesRv) {
                layoutManager = LinearLayoutManager(context)
                adapter = currentRatesAdapter
                (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

                // Scroll to first item after change
                currentRatesAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
                    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                    }
                })
            }
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

//    private fun showSnackbar() {
//        internetConnectionManager.isInternetAvailable(activity.parent)
//
//
//    }

//    private fun setView() {
//
//        showSnackbar(findViewById(android.R.id.content))
//
//        currentRatesAdapter = CurrentRatesListAdapter(this, this)
//
//        with(currenciesRv) {
//            layoutManager = LinearLayoutManager(context)
//            adapter = currentRatesAdapter
//            setHasFixedSize(true)
//
//            (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//            currenciesRv.itemAnimator = null
//
//
//            currentRatesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
//                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
//                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
//                }
//            })
//
//        }
//    }

}
