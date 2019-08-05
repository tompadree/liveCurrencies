package com.currencytrackingapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import com.currencytrackingapp.utils.network.InternetConnectionManager


class InternetConnectionManagerImpl(private val context: Context) : InternetConnectionManager {

    override fun hasInternetConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}