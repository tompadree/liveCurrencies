package com.currencytrackingapp.data.api

import com.currencytrackingapp.data.models.RatesObject
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Query

interface NetworkApi {

    fun getLatestRates(base: String): Deferred<Response<RatesObject>>

//    suspend fun getCurrencies(): RatesObject

}