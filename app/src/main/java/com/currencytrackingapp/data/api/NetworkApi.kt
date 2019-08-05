package com.currencytrackingapp.data.api

import com.currencytrackingapp.data.models.Rates
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Query

interface NetworkApi {

    fun getLatestRates(base: String): Deferred<Response<Rates>>

}