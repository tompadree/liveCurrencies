package com.currencytrackingapp.data.source.remote.api

import com.currencytrackingapp.data.source.remote.api.APIConstants.Companion.CONTENT_TYPE_JSON
import com.currencytrackingapp.data.source.remote.api.APIConstants.Companion.LATEST_RATES
import com.currencytrackingapp.data.models.RatesObject
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface RevolutApi {

    @Headers(CONTENT_TYPE_JSON)
    @GET(LATEST_RATES)
    fun getLatestRates(@Query("base") base: String): Deferred<Response<RatesObject>>

    @Headers(CONTENT_TYPE_JSON)
    @GET(LATEST_RATES)
    suspend fun getLatestCurrencies(@Query("base") base: String): Response<RatesObject>

}