package com.currencytrackingapp.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.currencytrackingapp.R
import com.currencytrackingapp.data.api.NetworkApi
import com.currencytrackingapp.data.models.Rates
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.data.models.ResponseError
import com.currencytrackingapp.data.models.ResponseSucces
import com.currencytrackingapp.utils.helpers.RequestExecutor
import com.currencytrackingapp.utils.SingleLiveEvent
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.network.InternetConnectionException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class CurrenciesViewModel(private val networkApi: NetworkApi, private val appUtils: AppUtils) : BaseViewModel() { //, KoinComponent {

//    private val networkApi: NetworkApi by inject()
//    private val appUtils: AppUtils by inject()

    private val currentRates = MutableLiveData<Rates>()
    private val currentRatesUpdatedEvent = SingleLiveEvent<Unit>()

    val _currentList = MutableLiveData<LinkedList<RatesListItem>>()

    private val _ratesListFetched = MutableLiveData<LinkedList<RatesListItem>>()
    val ratesListFetched: LiveData<LinkedList<RatesListItem>> get() = _ratesListFetched

    val currentBase = ObservableField<String>("EUR")
    val currentValue = ObservableField<String>("100")
    val fetchingFlag = ObservableField<Boolean>(false)

    private val userObserver = Observer<Rates> {
        currentRatesUpdatedEvent.postCall()
    }

    init {
        showLoading(true)
        _currentList.value = LinkedList()
        currentRates.observeForever(userObserver)
        fetchingFlag.set(true)
        fetchRates()
    }

    fun resume(){
        fetchingFlag.set(true)
    }

    fun pause(){
        fetchingFlag.set(false)
    }

    fun destroy() {
        fetchingFlag.set(false)
        onCleared()
    }


    override fun onCleared() {
        try {
            currentRates.removeObserver(userObserver)
        } finally {
            super.onCleared()
        }
    }

    private fun fetchRates() {

        viewModelScope.launch {
            while (fetchingFlag.get() == true) {
                delay(1_000)
                val currentList = _currentList.value?: LinkedList()
                val currentBase = currentBase.get()?: "EUR"
                val currentValue = currentValue.get()?: "100"

                when (val response = RequestExecutor.execute(networkApi.getLatestRates(currentBase))) { /*TODO BASE*/
                    is ResponseSucces -> {

                        response.data.body()?.let {

                            val returnList = LinkedList<RatesListItem>()
                            val isInit = currentList.isEmpty()

                            if (!isInit) {
                                returnList.addFirst(RatesListItem(currentBase, currentValue.toDouble()))
                                for (i in 1 until currentList.size) {
                                    try {
                                        if(it.rates.containsKey(currentList[i].name))
                                        returnList.add(RatesListItem(currentList[i].name, roundOffDecimal(currentValue.toDouble() * it.rates[currentList[i].name]!!)))
                                    } catch ( e: Exception) {
                                        fail(e)
                                    }
                                }
                            } else {
                                returnList.addFirst(RatesListItem(appUtils.getString(R.string.eur_), 100.00))
                                for (item in it.rates) {
                                    returnList.add(RatesListItem(item.key, roundOffDecimal(item.value * 100)))
                                }
                            }

                            val sorted = when (isInit) {
                                true -> firstTimeSorting(currentBase, returnList)
                                else -> returnList
                            }

                            _currentList.value = sorted
                            _ratesListFetched.postValue(sorted)
                            showLoading(false)
                            postLoading(false)
                        }

                    }
                    is ResponseError -> {
                        showLoading(false)
                        fail(response.t as Exception)
                        postLoading(false)
                        if(response.t !is InternetConnectionException)
                            handleError(response.t)
                    }
                }
            }
        }

    }

    private fun firstTimeSorting(base: String, returnList: LinkedList<RatesListItem>): LinkedList<RatesListItem> {
        val sorted = LinkedList<RatesListItem>(returnList.sortedWith(compareBy { it.name }))
        sorted.remove(RatesListItem(base, 100.00))
        sorted.addFirst(RatesListItem(base, 100.00))
        return sorted
    }

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }

    private fun fail(e: Exception?) {
        resetError()

    }

    private fun resetError() {
    }

    private fun showLoading(show: Boolean){
        setLoading(show)

    }



}