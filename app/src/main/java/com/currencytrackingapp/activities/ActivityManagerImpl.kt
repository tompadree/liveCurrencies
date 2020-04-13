package com.currencytrackingapp.activities

import android.app.Activity
import android.content.Intent
import com.currencytrackingapp.currencies.CurrenciesActivity

class ActivityManagerImpl(val activity: Activity) : ActivityManager {

    override fun openCurrenciesActivity() {
        activity.startActivity(Intent(activity, CurrenciesActivity::class.java))
    }
}