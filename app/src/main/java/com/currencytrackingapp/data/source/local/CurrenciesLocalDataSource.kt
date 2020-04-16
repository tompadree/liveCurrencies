package com.currencytrackingapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.source.CurrenciesDataStore
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.models.Result.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class CurrenciesLocalDataSource(private val currenciesDao: CurrenciesDao) : CurrenciesDataStore {

    override fun observeRates(): LiveData<Result<RatesObject>> {
        return currenciesDao.observeRates().map {
            it?.let { Success(it) }
        }
    }

    override suspend fun getLatestRates(base: String): Result<RatesObject> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Success(currenciesDao.getCurrencies())
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun refreshRates(base: String) {}

    override suspend fun saveTasks(rates: RatesObject) {
        currenciesDao.deleteRates()
        currenciesDao.insertRates(rates)
    }
}
