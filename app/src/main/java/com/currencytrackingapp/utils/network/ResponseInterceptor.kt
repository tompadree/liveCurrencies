package com.currencytrackingapp.utils.network

import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor(
    private val internetConnectionManager: InternetConnectionManager,
    private val notAuthorizedHandler: NotAuthorizedHandler
) : Interceptor {


    private val UNAUTHORIZED_EXCEPTION_CODE = 401


    override fun intercept(chain: Interceptor.Chain?): Response {
        try {
            if (!internetConnectionManager.hasInternetConnection()) {
                throw InternetConnectionException()
            }

            val request = chain!!.request()
            val response = chain.proceed(request)

            val responseCode = response.code()

            if (responseCode > 399) {
                if (responseCode == UNAUTHORIZED_EXCEPTION_CODE) {
                    notAuthorizedHandler.logout()
                }

                throw NetworkException(response)
            }

            return response
        } catch (e: InternetConnectionException) {
            throw e
        } catch (e: NetworkException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw NetworkException(null)
        }
    }
}