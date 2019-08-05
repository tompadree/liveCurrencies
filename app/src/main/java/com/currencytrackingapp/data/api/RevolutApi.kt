package com.currencytrackingapp.data.api

import com.currencytrackingapp.data.api.APIConstants.Companion.CONTENT_TYPE_JSON
import com.currencytrackingapp.data.api.APIConstants.Companion.LATEST_RATES
import com.currencytrackingapp.data.models.Rates
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface RevolutApi {

    @Headers(CONTENT_TYPE_JSON)
    @GET(LATEST_RATES)
    fun getLatestRates(@Query("base") base: String): Deferred<Response<Rates>>

}