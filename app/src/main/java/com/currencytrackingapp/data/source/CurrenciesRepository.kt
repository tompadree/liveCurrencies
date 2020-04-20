package com.currencytrackingapp.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result

/**
 * @author Tomislav Curis
 */
interface CurrenciesRepository {

    fun observeRates(): LiveData<Result<RatesObject>>

    suspend fun getLatestRates(forceUpdate: Boolean = false, base: String = "EUR"): Result<RatesObject>

//    suspend fun refreshRates(base: String)

    suspend fun saveRates(rates: RatesObject)

}