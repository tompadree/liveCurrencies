package com.currencytrackingapp.viewmodel

import android.util.Log
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
import com.currencytrackingapp.utils.RequestExecutor
import com.currencytrackingapp.utils.SingleLiveEvent
import com.currencytrackingapp.utils.helpers.AppUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class CurrenciesViewModel : BaseViewModel(), KoinComponent {

    private val networkApi: NetworkApi by inject()
    private val appUtils: AppUtils by inject()

    val currentRates = MutableLiveData<Rates>()
    private val currentRatesUpdatedEvent = SingleLiveEvent<Unit>()

    val _currentList = MutableLiveData<LinkedList<RatesListItem>>()

    val _ratesListFetched = MutableLiveData<LinkedList<RatesListItem>>()
    val ratesListFetched: LiveData<LinkedList<RatesListItem>> get() = _ratesListFetched

    val currentBase = ObservableField<String>("EUR")
    val currentValue = ObservableField<String>("100")

    private val userObserver = Observer<Rates> {
        currentRatesUpdatedEvent.postCall()
    }

    init {
        _currentList.value = LinkedList()
        currentRates.observeForever(userObserver)
        fetchRates()
    }

    override fun onCleared() {
        try {
            currentRates.removeObserver(userObserver)
        } finally {
            super.onCleared()
        }
    }

    fun fetchRatesCOnstant() {
        viewModelScope.launch {
            while(true) {
                delay(1_000)
                // do something every second
                Log.e("TEST", "COROUTINE FIRED")
            }
        }
    }

    fun fetchRates() { //base: String, baseValue: String, currentList: LinkedList<RatesListItem>) {

        viewModelScope.launch {
            while (true) {
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
                                        e.printStackTrace()
                                        Log.e("ERROR", "currentList[i].name = " + currentList[i].name)
                                        for(rate in it.rates)
                                        {
                                            Log.e("ERROR", "rate = $rate")
                                        }// + "  it.rates[currentList[i].name]!! = " + it.rates[currentList[i].name])
                                    }
//                                returnList[i].currentRate = baseValue.toDouble() * it.rates[currentList[i].name]!!
//                                returnList[i].currentRate = roundOffDecimal(currentList[i].currentRate)
                                }
                            } else {

                                returnList.addFirst(RatesListItem(appUtils.getString(R.string.eur_), 100.00))
                                for (item in it.rates) {
//                                if (item.key == it.base)
//                                    currentList.addFirst(RatesListItem(item.key, 100.00))
//                                else

                                    returnList.add(RatesListItem(item.key, roundOffDecimal(item.value * 100)))

                                }

                            }

                            val sorted = when (isInit) {
                                true -> firstTimeSorting(currentBase, returnList)
                                else -> returnList
                            }

                            _currentList.value = sorted
                            _ratesListFetched.postValue(sorted)
                        }

                    }
                    is ResponseError -> {
                        fail(response.t as Exception)
                        postLoading(false)
                        handleError(response.t)
                    }
                }
            }
        }

    }

    private fun firstTimeSorting(base: String, returnList: LinkedList<RatesListItem>): LinkedList<RatesListItem> {
        val sorted = LinkedList<RatesListItem>(returnList.sortedWith(compareBy({ it.name })))
        sorted.remove(RatesListItem(base, 100.00))
        sorted.addFirst(RatesListItem(base, 100.00))
        return sorted
    }

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }


    data class MyDate(val year: Int, val month: Int, val day: Int)

    private fun fail(e: Exception?) {
        postLoading(false)
//        if (e is UserNotConfirmedException) {
//            openVerificationOpenAccount.value = login
//        } else {
        resetError()
//        _logoutError.postValue(e)
//        }
    }

    private fun resetError() {
//        _logoutError.postValue(null)
//        passwordError.set(null)
    }

    fun showLoading(show: Boolean) {
        setLoading(show)
    }

}