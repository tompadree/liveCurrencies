package com.currencytrackingapp.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.source.local.CurrenciesLocalDataSource
import com.currencytrackingapp.data.source.remote.CurrenciesRemoteDataSource
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.models.Result.Success
import com.currencytrackingapp.data.models.Result.Error
import com.currencytrackingapp.utils.wrapEspressoIdlingResource
import retrofit2.Response

/**
 * @author Tomislav Curis
 */
class CurrenciesRepositoryImpl(private val currenciesLocalDataSource: CurrenciesLocalDataSource,
                               private val currenciesRemoteDataSource: CurrenciesRemoteDataSource) : CurrenciesRepository {

    override fun observeRates(): LiveData<Result<RatesObject>> {
        wrapEspressoIdlingResource {
            return currenciesLocalDataSource.observeRates()
        }
    }

    override suspend fun getLatestRates(forceUpdate: Boolean, base: String): Result<RatesObject> {
        // for test is it running or not 1 or 0
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                try {
                    updateRatesFromRemoteDataSource(base)
                } catch (ex: Exception) {
                    return Error(ex)
                }
            }
            return currenciesLocalDataSource.getLatestRates("")
        }
    }

//    override suspend fun refreshRates(base: String) {
//        wrapEspressoIdlingResource {
//            updateRatesFromRemoteDataSource(base)
//        }
//    }

    override suspend fun saveRates(rates: RatesObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun updateRatesFromRemoteDataSource(base: String){

        val remoteRates = currenciesRemoteDataSource.getLatestRates(base)
        if (remoteRates is Success) {
            currenciesLocalDataSource.saveRates(remoteRates.data)

        } else if (remoteRates is Error) {

            throw remoteRates.exception
        }


    }


}