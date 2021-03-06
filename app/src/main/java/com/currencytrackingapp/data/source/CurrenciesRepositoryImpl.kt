package com.currencytrackingapp.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.models.Result.Error
import com.currencytrackingapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * @author Tomislav Curis
 */
class CurrenciesRepositoryImpl(
    private val currenciesLocalDataSource: CurrenciesDataSource,
    private val currenciesRemoteDataSource: CurrenciesDataSource) : CurrenciesRepository {

    override fun observeRates(): LiveData<Result<RatesObject>> {
        wrapEspressoIdlingResource {
            return currenciesLocalDataSource.observeRates()
        }
    }

    override suspend fun getLatestRates(forceUpdate: Boolean, base: String): Result<RatesObject> {
        // Wrapper for test is it running or not 1 or 0
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                try {
                    updateRatesFromRemoteDataSource(base)
                } catch (ex: Exception) {
                    return Error(ex)
                }
            }
            return currenciesLocalDataSource.getLatestRates(base)
        }
    }

    override suspend fun saveRates(rates: RatesObject) {
        coroutineScope {
            try {
                launch { currenciesLocalDataSource.saveRates(rates) }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun deleteRates() {
        coroutineScope {
            try {
                launch { currenciesLocalDataSource.deleteRates() }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private suspend fun updateRatesFromRemoteDataSource(base: String) {
        wrapEspressoIdlingResource {
            val remoteRates = currenciesRemoteDataSource.getLatestRates(base)
            if (remoteRates is Success) {
                currenciesLocalDataSource.saveRates(remoteRates.data)
            } else if (remoteRates is Error) {
                throw remoteRates.exception
            }
        }
    }
}