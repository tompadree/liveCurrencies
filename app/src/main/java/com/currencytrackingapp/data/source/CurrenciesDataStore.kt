package com.currencytrackingapp.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result

/**
 * @author Tomislav Curis
 */
interface CurrenciesDataStore {

    fun observeRates(): LiveData<Result<RatesObject>>

    suspend fun getLatestRates(base: String): Result<RatesObject>

    suspend fun refreshRates()

    suspend fun saveTasks(rates: RatesObject)
}