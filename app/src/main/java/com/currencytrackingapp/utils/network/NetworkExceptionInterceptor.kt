package com.currencytrackingapp.utils.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author Tomislav Curis
 */

// https://stackoverflow.com/questions/60278936/throwing-ioexception-in-interceptor-is-causing-the-crash-with-retrofit
class NetworkExceptionInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        when (response.isSuccessful) {
            true  -> return response
            false -> {

                throw NetworkException(response)
            }
        }

    }
}