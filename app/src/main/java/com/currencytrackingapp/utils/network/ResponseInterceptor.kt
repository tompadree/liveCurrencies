package com.currencytrackingapp.utils.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ResponseInterceptor(
    private val internetConnectionManager: InternetConnectionManager) : Interceptor {


    private val UNAUTHORIZED_EXCEPTION_CODE = 401


    override fun intercept(chain: Interceptor.Chain?): Response {
        try {
//            if (!internetConnectionManager.hasInternetConnection()) {
//                throw IOException()
//            }

            val request = chain!!.request()
            val response = chain.proceed(request)

            val responseCode = response.code()

            if (responseCode > 399) {
                if (responseCode == UNAUTHORIZED_EXCEPTION_CODE) {

                }

                throw NetworkException(response)
            }

            return response
        } catch (e: NoInternetException) {
            throw e
        } catch (e: NetworkException) {
            throw e
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw NetworkException(null)
        }
    }
}