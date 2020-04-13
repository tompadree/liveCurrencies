package com.currencytrackingapp.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.currencytrackingapp.data.api.NetworkApi
import com.currencytrackingapp.data.api.RevolutApi
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.ResponseError
import com.currencytrackingapp.data.models.ResponseSucces
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.source.CurrenciesDataStore
import com.currencytrackingapp.utils.helpers.RequestExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class CurrenciesRemoteDataSource(private val revolutApi: RevolutApi) : CurrenciesDataStore {

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
            IOException("Error loading data " +
                "${response.code()} ${response.message()}")
        )
    }

    override suspend fun refreshRates() {
        observableRates.value = getLatestRates("EUR")
    }

    override suspend fun saveTasks(rates: RatesObject) {}
}