package com.currencytrackingapp.utils.network

import android.content.BroadcastReceiver
import android.view.View

interface InternetConnectionManager {

    fun hasInternetConnection(): Boolean

    fun isInternetAvailable(mParentLayout: View) : BroadcastReceiver

}