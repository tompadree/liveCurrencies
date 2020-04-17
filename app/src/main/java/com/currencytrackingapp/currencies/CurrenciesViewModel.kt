package com.currencytrackingapp.currencies

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.source.CurrenciesRepository
import com.currencytrackingapp.utils.network.InternetConnectionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap

class CurrenciesViewModel(private val repository: CurrenciesRepository, private val internetConnectionManager: InternetConnectionManager) : ViewModel() { //} BaseViewModel() { //, KoinComponent {


    val _currentBase = ObservableField<String>("EUR")
    val _currentValue = ObservableField<String>("100")

    // Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    // Not used at the moment
    val _isInternetConnected = MutableLiveData<Boolean>(true)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val _forceUpdate = MutableLiveData<Boolean>(false)

    // https://stackoverflow.com/questions/47575961/what-is-the-difference-between-map-and-switchmap-methods
    private val _items: LiveData<List<RatesListItem>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate && internetConnectionManager.hasInternetConnection()) {
            _dataLoading.value = true
            viewModelScope.launch {
                repository.refreshRates(_currentBase.get()!!)
                _dataLoading.value = false
            }
        }
        // first fetched locally from last time than updated from backend
        repository.observeRates().switchMap {
            filterRates(it)
        }
    }

    val items: LiveData<List<RatesListItem>> = _items

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    var hasInternet = MutableLiveData<Boolean>(false)


    init {
        constantRefresh()
    }

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
        _forceUpdate.value = internetConnectionManager.hasInternetConnection()
    }

    private fun constantRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
//                refresh()
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
        val currentValue = if(_currentValue.get()!!.isNotEmpty()) _currentValue.get()?: "100" else "0.0"
        val returnList =  LinkedList<RatesListItem>()


        for (item in rates) {
            try {
                if(rates.containsKey(item.key) && item.key != _currentBase.get())
                    returnList.add(RatesListItem(item.key, roundOffDecimal(currentValue.toDouble() * rates[item.key]!!)))
            } catch ( e: Exception) {
                fail(e)
            }
        }

        return sorting(currentValue.toDouble(), _currentBase.get()!!, returnList)

    }


    fun onItemClicked(ratesItem: RatesListItem){
        _currentBase.set(ratesItem.name)
        _currentValue.set(ratesItem.currentRate.toString())
        _forceUpdate.value = true
    }

    fun onBaseChanged(value: String) {
        _currentValue.set(value)
        _forceUpdate.value = true
    }

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