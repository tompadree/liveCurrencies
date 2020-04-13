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
                    updateRatesFromRemoteDataSource()
                } catch (ex: Exception) {
                    return Error(ex)
                }
            }
            return currenciesLocalDataSource.getLatestRates("")
        }
    }

    override suspend fun refreshRates() {
        wrapEspressoIdlingResource {
            updateRatesFromRemoteDataSource()
        }
    }

    override suspend fun saveTasks(rates: RatesObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun updateRatesFromRemoteDataSource(){
        val remoteRates = currenciesRemoteDataSource.getLatestRates("EUR")

        if (remoteRates is Success) {
            // Real apps might want to do a proper sync, deleting, modifying or adding each task.
//            tasksLocalDataSource.deleteAllTasks()
            currenciesLocalDataSource.saveTasks(remoteRates.data)
//            remoteRates.data.rates.forEach { task ->
//                tasksLocalDataSource.saveTask(task)
//            }
        } else if (remoteRates is Error) {
            throw remoteRates.exception
        }
    }


}