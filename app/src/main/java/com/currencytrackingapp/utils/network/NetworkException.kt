package com.currencytrackingapp.utils.network

import com.currencytrackingapp.data.models.NetworkErrors
import com.google.gson.Gson
import okhttp3.Response
import java.io.IOException

class NetworkException(response: Response?) : IOException() {

    private val localResponse: okhttp3.Response? = response
    private var errors: NetworkErrors? = localResponse?.let { handleResponse(localResponse) }
    override var message: String = localResponse?.let { getMessage(localResponse) } ?: "Some error"


    private fun handleResponse(response: Response): NetworkErrors? {
        return try {
            val errorBodyJson = response.body()?.string() ?: "{}"
            message = Gson().fromJson(errorBodyJson, NetworkErrors::class.java).messages!![0]
             Gson().fromJson(errorBodyJson, NetworkErrors::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun getMessage(response: Response): String? {
        return try {
            if(message != "Some error")
                return message
            val errorBodyJson = response.body()?.string() ?: "{}"
            Gson().fromJson(errorBodyJson, NetworkErrors::class.java).messages!![0]
        } catch (e: Exception) {
            null
        }
    }

    fun getErrors(): NetworkErrors? {
        return errors
    }
}