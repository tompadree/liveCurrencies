package com.currencytrackingapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.source.CurrenciesDataSource
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.models.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class CurrenciesLocalDataSource(private val currenciesDao: CurrenciesDao, private val dispatchers: CoroutineDispatcher = Dispatchers.IO)
    : CurrenciesDataSource {
    override fun observeRates(): LiveData<Result<RatesObject>> {
        return currenciesDao.observeRates().map {
            it?.let { Success(it) }
        }
    }

    override suspend fun getLatestRates(base: String): Result<RatesObject> =
        withContext(dispatchers) {
            return@withContext try {
                Success(currenciesDao.getCurrencies())
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun refreshRates(base: String) {}

    override suspend fun saveRates(rates: RatesObject) = withContext(dispatchers) {
        currenciesDao.deleteRates()
        currenciesDao.insertRates(rates)
    }

    override suspend fun deleteRates() = withContext(dispatchers) {
        currenciesDao.deleteRates()
    }
}
