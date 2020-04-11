package com.currencytrackingapp.utils.network

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.currencytrackingapp.view.activities.splash.SplashActivity

class NotAuthorizedHandlerImpl(
    private val applicationContext: Context
) : NotAuthorizedHandler {

    override fun logout() {
        try {
            val component = ComponentName(applicationContext, SplashActivity::class.java)
            val intent = Intent.makeRestartActivityTask(component)

            applicationContext.startActivity(intent)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}