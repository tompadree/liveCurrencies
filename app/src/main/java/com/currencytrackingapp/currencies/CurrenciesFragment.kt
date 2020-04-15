package com.currencytrackingapp.currencies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.currencytrackingapp.R
import com.currencytrackingapp.databinding.FragmentCurrenciesBinding
import com.currencytrackingapp.base.BindingFragment
import com.currencytrackingapp.base.NavigationActivity
import kotlinx.android.synthetic.main.fragment_currencies.*
import timber.log.Timber

class CurrenciesFragment : BindingFragment<FragmentCurrenciesBinding>() {


    override val layoutId = R.layout.fragment_currencies

    private val viewModel: CurrenciesViewModel by viewModel()

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
//        viewModel = ViewModelProviders.of(this).get(LoginEnvViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        setupRv()


//        FragmentCurrenciesBinding.bind(view)
    }

    fun setupRv() {
        if (viewModel != null) {
            currentRatesAdapter = RatesListAdapter(viewModel, activity as NavigationActivity)
            with(currenciesRv) {
                adapter = currentRatesAdapter
                (currenciesRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }


    }

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
