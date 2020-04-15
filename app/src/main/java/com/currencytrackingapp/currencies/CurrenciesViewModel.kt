package com.currencytrackingapp.currencies

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.source.CurrenciesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap

class CurrenciesViewModel(private val repository: CurrenciesRepository) : ViewModel() { //} BaseViewModel() { //, KoinComponent {

//    private val networkApi: NetworkApi by inject()
//    private val appUtils: AppUtils by inject()
//private val repository: CurrenciesRepository? = null //CurrenciesRepositoryImpl(null, null)
//    private val currentRates = MutableLiveData<RatesObject>()
//    private val currentRatesUpdatedEvent = SingleLiveEvent<Unit>()
//
//    val _currentList = MutableLiveData<LinkedList<RatesListItem>>()
//
//    private val _ratesListFetched = MutableLiveData<LinkedList<RatesListItem>>()
//    val ratesListFetched: LiveData<LinkedList<RatesListItem>> get() = _ratesListFetched
//
//    val currentBase = ObservableField<String>("EUR")
    val _currentValue = ObservableField<String>("100")
//    set(value) = ""
//    var currentValue: String
//        get() = _currentValue.get()!!
//        set(value) = _currentValue.set("100")


//    val fetchingFlag = ObservableField<Boolean>(false)






    // Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<RatesListItem>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                repository.refreshRates()
                _dataLoading.value = false
            }
        }
        // first fetched locally from last time than updated from backend
        repository.observeRates().switchMap {
            filterRates(it)
        }
    }

    val items: LiveData<List<RatesListItem>> = _items

//    private val userObserver = Observer<RatesObject> {
//        currentRatesUpdatedEvent.postCall()
//    }

    init {
//        showLoading(true)
//        _currentList.value = LinkedList()
//        currentRates.observeForever(userObserver)
//        fetchingFlag.set(true)
//        fetchRates(false)
//        fetchRates(true)
        constantRefresh()
    }

//    fun resume(){
//        fetchingFlag.set(true)
//    }
//
//    fun pause(){
//        fetchingFlag.set(false)
//    }
//
//    fun destroy() {
//        fetchingFlag.set(false)
//        onCleared()
//    }


    override fun onCleared() {
        try {
//            currentRates.removeObserver(userObserver)
        } finally {
            super.onCleared()
        }
    }

    fun fetchRates(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun refresh() {
        _forceUpdate.value = true
    }

    private fun constantRefresh() {

        viewModelScope.launch {
            while(true) {
                delay(1000)
                _forceUpdate.value = true
            }
        }
    }

    private fun filterRates(ratesResult: Result<RatesObject>): LiveData<List<RatesListItem>> {
        // TODO: This is a good case for liveData builder. Replace when stable.
        val result = MutableLiveData<List<RatesListItem>>()

        if (ratesResult is Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(ratesResult.data.rates)
            }
        } else {
//            result.value = emptyList()
//            showSnackbarMessage(R.string.loading_tasks_error)
            isDataLoadingError.value = true
        }
        return result
    }

    private fun filterItems(rates: HashMap<String, Double>): List<RatesListItem> {
        // We filter the tasks based on the requestType
        val currentValue = _currentValue.get()?: "100"
        val returnList =  LinkedList<RatesListItem>()


        for (item in rates) {
            try {
                if(rates.containsKey(item.key) && item.key != "EUR")
                    returnList.add(RatesListItem(item.key, roundOffDecimal(currentValue.toDouble() * rates[item.key]!!)))
            } catch ( e: Exception) {
                fail(e)
            }
        }

        return sorting(currentValue.toDouble(), "EUR", returnList)
//        return if(currentValue == "100") firstTimeSorting("EUR",returnList) else returnList
    }


    interface EditTextListener {

    }

    fun onBaseChanged(value: String) {
        _currentValue.set(value)
//        items.value?.get(0)?.let{ it.currentRate = value.toDouble()}
        _forceUpdate.value = true
    }

//    private fun fetchRates() {
//
//        viewModelScope.launch {
//
//
//
//
//            while (fetchingFlag.get() == true) {
//                delay(1_000)
//                val currentList = _currentList.value?: LinkedList()
//                val currentBase = currentBase.get()?: "EUR"
//                val currentValue = currentValue.get()?: "100"
//
//                when (val response = RequestExecutor.execute(networkApi.getLatestRates(currentBase))) { /*TODO BASE*/
//                    is ResponseSucces -> {
//
//                        response.data.body()?.let {
//
//                            val returnList = LinkedList<RatesListItem>()
//                            val isInit = currentList.isEmpty()
//
//                            if (!isInit) {
//                                returnList.addFirst(RatesListItem(currentBase, currentValue.toDouble()))
//                                for (i in 1 until currentList.size) {
//                                    try {
//                                        if(it.rates.containsKey(currentList[i].name))
//                                        returnList.add(RatesListItem(currentList[i].name, roundOffDecimal(currentValue.toDouble() * it.rates[currentList[i].name]!!)))
//                                    } catch ( e: Exception) {
//                                        fail(e)
//                                    }
//                                }
//                            } else {
//                                returnList.addFirst(RatesListItem(appUtils.getString(R.string.eur_), 100.00))
//                                for (item in it.rates) {
//                                    returnList.add(RatesListItem(item.key, roundOffDecimal(item.value * 100)))
//                                }
//                            }
//
//                            val sorted = when (isInit) {
//                                true -> firstTimeSorting(currentBase, returnList)
//                                else -> returnList
//                            }
//
//                            _currentList.value = sorted
//                            _ratesListFetched.postValue(sorted)
//                            showLoading(false)
//                            postLoading(false)
//                        }
//
//                    }
//                    is ResponseError -> {
//                        showLoading(false)
//                        fail(response.t as Exception)
//                        postLoading(false)
//                        if(response.t !is InternetConnectionException)
//                            handleError(response.t)
//                    }
//                }
//            }
//        }
//
//    }

    private fun firstTimeSorting(base: String, returnList: LinkedList<RatesListItem>): LinkedList<RatesListItem> {
        val sorted = LinkedList<RatesListItem>(returnList.sortedWith(compareBy { it.name }))
        sorted.remove(RatesListItem(base, 100.00))
        sorted.addFirst(RatesListItem(base, 100.00))
        return sorted
    }

    private fun sorting(firstValue : Double, base: String, returnList: LinkedList<RatesListItem>): LinkedList<RatesListItem> {
        val sorted = LinkedList<RatesListItem>(returnList.sortedWith(compareBy { it.name }))
        sorted.remove(RatesListItem(base, firstValue))
        sorted.addFirst(RatesListItem(base, firstValue))
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
//        setLoading(show)

    }



}