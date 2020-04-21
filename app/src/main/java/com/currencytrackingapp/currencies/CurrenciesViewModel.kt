package com.currencytrackingapp.currencies

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.currencytrackingapp.R
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.source.CurrenciesRepository
import com.currencytrackingapp.utils.SingleLiveEvent
import com.currencytrackingapp.utils.network.InternetConnectionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap


class CurrenciesViewModel(private val repository: CurrenciesRepository, private val internetConnectionManager: InternetConnectionManager) : ViewModel() { //} BaseViewModel() { //, KoinComponent {

    val _currentBase = ObservableField<String>("EUR")
    val _currentValue = ObservableField<String>("100")

    private val _snackbarText = SingleLiveEvent<Int>()
//    val snackbarText: LiveData<Int> = _snackbarText

    val isDataLoadingError = MutableLiveData<Boolean>(false)


    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    // https://stackoverflow.com/questions/47575961/what-is-the-difference-between-map-and-switchmap-methods
    private val _items: LiveData<List<RatesListItem>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate && internetConnectionManager.hasInternetConnection()) {
//            _dataLoading.value = true
            viewModelScope.launch {
                // RefreshRates don't have error handle
//                repository.refreshRates(_currentBase.get()!!)
                handleResponseWithError(repository.getLatestRates(forceUpdate, _currentBase.get()!!))
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

    fun initVM() {
        _dataLoading.value = true
        constantRefresh()
    }

//    fun fetchRates(forceUpdate: Boolean) {
////        _dataLoading.value = true
//        _forceUpdate.value = forceUpdate
//    }

    fun refresh(loading: Boolean) {
        val isInternet = internetConnectionManager.hasInternetConnection() && !isDataLoadingError.value!!
        _dataLoading.value = isInternet && loading
        _forceUpdate.value = isInternet
    }

    // Service not used for the sake of the MVVM demo
    fun constantRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                refresh(false)
//                fetchRates(!isDataLoadingError.value!!)
            }
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.postValue(message)
    }

    // filter response from DB
    private fun filterRates(ratesResult: Result<RatesObject>): LiveData<List<RatesListItem>> {
        // TODO: This is a good case for liveData builder. Replace when stable.
        val result = MutableLiveData<List<RatesListItem>>()

        if (ratesResult is Success) {
//            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(ratesResult.data.rates)
                showSnackbarMessage(R.string.error_default_db)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.error_default_db)
//            isDataLoadingError.value = true
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
                _error.postValue(e)
            }
        }

        return sorting(currentValue.toDouble(), _currentBase.get()!!, returnList)

    }


    protected fun <T> handleResponseWithError(response: Result<T>): T? {
        return when (response) {
            is Success -> {
                isDataLoadingError.value = false
                response.data
            }
            is Result.Error -> {
                isDataLoadingError.value = true
                _error.postValue(response.exception)
                null
            }
            is Result.Loading -> null
        }
    }


    fun onItemClicked(ratesItem: RatesListItem){
        _currentBase.set(ratesItem.name)
        _currentValue.set(ratesItem.currentRate.toString())
        _forceUpdate.value = internetConnectionManager.hasInternetConnection()
    }

    fun onBaseChanged(value: String) {
        _currentValue.set(value)
        _forceUpdate.value = internetConnectionManager.hasInternetConnection()
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
}