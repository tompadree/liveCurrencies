package com.currencytrackingapp.data.api.impl

import com.currencytrackingapp.data.api.NetworkApi
import com.currencytrackingapp.data.api.RevolutApi
import com.currencytrackingapp.data.models.Rates
import kotlinx.coroutines.Deferred
import retrofit2.Response

class NetworkApiImpl(private val revolutApi: RevolutApi): NetworkApi {

    override fun getLatestRates(base: String): Deferred<Response<Rates>> = revolutApi.getLatestRates(base)
}