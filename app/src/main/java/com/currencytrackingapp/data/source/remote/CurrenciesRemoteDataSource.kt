package com.currencytrackingapp.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.currencytrackingapp.data.source.remote.api.RevolutApi
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.source.CurrenciesDataSource
import java.io.IOException

/**
 * @author Tomislav Curis
 */
class CurrenciesRemoteDataSource(private val revolutApi: RevolutApi) : CurrenciesDataSource {

    private val observableRates = MutableLiveData<Result<RatesObject>>()

    override fun observeRates(): LiveData<Result<RatesObject>> = observableRates

    override suspend fun getLatestRates(base: String): Result<RatesObject> {
            val response = revolutApi.getLatestCurrencies(base)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Result.Success(body)
                }
            }
            return Result.Error(
                IOException("Error loading data " + "${response.code()} ${response.message()}")
            )
    }

    override suspend fun refreshRates(base: String) {
        observableRates.value = getLatestRates(base)
    }

    override suspend fun saveRates(rates: RatesObject) {}
}