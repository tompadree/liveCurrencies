package com.currencytrackingapp.data.api.impl

import com.currencytrackingapp.data.api.NetworkApi
import com.currencytrackingapp.data.api.RevolutApi
import com.currencytrackingapp.data.models.RatesObject
import kotlinx.coroutines.Deferred
import retrofit2.Response

class NetworkApiImpl(private val revolutApi: RevolutApi): NetworkApi {

    override fun getLatestRates(base: String): Deferred<Response<RatesObject>> = revolutApi.getLatestRates(base)

//    override suspend fun getCurrencies(): Response<RatesObject> = revolutApi.getLatestCurrencies("EUR")
}