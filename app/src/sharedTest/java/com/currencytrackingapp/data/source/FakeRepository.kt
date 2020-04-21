package com.currencytrackingapp.data.source

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
@ExperimentalCoroutinesApi
class FakeRepository : CurrenciesRepository {

    companion object {
        // Dataset
        val dummyRates: HashMap<String, Double> =
            hashMapOf(
                "AUD" to 1.6178,
                "BGN" to 1.95,
                "BRL" to 4.7958,
                "CAD" to 1.5351,
                "CHF" to 1.1285,
                "CNY" to 7.9518,
                "CZK" to 25.737,
                "DKK" to 7.463,
                "GBP" to 0.899,
                "HKD" to 9.1401,
                "HRK" to 7.4404,
                "HUF" to 326.77,
                "IDR" to 17338.00,
                "ILS" to 4.1741,
                "INR" to 83.788,
                "ISK" to 127.91,
                "JPY" to 129.66,
                "KRW" to 1305.9,
                "MXN" to 22.384,
                "MYR" to 4.8161,
                "NOK" to 9.7842,
                "NZD" to 1.7648,
                "PHP" to 62.645,
                "PLN" to 4.3219,
                "RON" to 4.6424,
                "RUB" to 79.642,
                "SEK" to 10.6,
                "SGD" to 1.6013,
                "THB" to 38.162,
                "TRY" to 7.6346,
                "USD" to 1.1644,
                "ZAR" to 17.838
            )

    }

    private var ratesObject = RatesObject(1, "EUR", "2018-09-06", dummyRates)

    private var shouldReturnError = false

    private val observableRates = MutableLiveData<Result<RatesObject>>()

    fun setReturnError(returnError: Boolean) {
        shouldReturnError = returnError
    }

    override fun observeRates(): LiveData<Result<RatesObject>> {
        runBlocking { observableRates.value = Result.Success(ratesObject) }
        return observableRates
    }

    override suspend fun getLatestRates(forceUpdate: Boolean, base: String): Result<RatesObject> {
        if (shouldReturnError)
            return Result.Error(Exception("Test exception"))
        // refresh
        runBlocking { observableRates.value = Result.Success(ratesObject) }

        return Result.Success(ratesObject)
    }

    override suspend fun saveRates(rates: RatesObject) {
        ratesObject = rates
//        runBlocking { observableRates.postValue(Result.Success(rates))}
    }

    override suspend fun deleteRates() {
        ratesObject = RatesObject(null, "", "", hashMapOf())
//        ratesObject.id = 0
//        ratesObject.base = ""
//        ratesObject.date = ""
//        ratesObject.rates.clear()
    }
}