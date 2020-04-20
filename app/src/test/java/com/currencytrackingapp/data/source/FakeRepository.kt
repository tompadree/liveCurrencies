package com.currencytrackingapp.data.source

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.source.CurrenciesRepositoryTest.Companion.dummyRates
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
@ExperimentalCoroutinesApi
class FakeRepository : CurrenciesRepository {

    var ratesObject = RatesObject(1, "EUR", "2018-09-06", dummyRates)

    private var shouldReturnError = false

    private val observableRates = MutableLiveData<Result<RatesObject>>()

    fun setReturnError(returnError: Boolean) {
        shouldReturnError = returnError
    }

    override fun observeRates(): LiveData<Result<RatesObject>> {
        return observableRates
    }

    override suspend fun getLatestRates(forceUpdate: Boolean, base: String): Result<RatesObject> {
        if (shouldReturnError)
            return Result.Error(Exception("Test exception"))
        // refresh
        observableRates.value = Result.Success(ratesObject)

        return Result.Success(ratesObject)
    }

    override suspend fun saveRates(rates: RatesObject) {
        observableRates.value = Result.Success(rates)
    }
}