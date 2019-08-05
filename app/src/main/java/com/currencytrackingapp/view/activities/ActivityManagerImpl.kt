package com.currencytrackingapp.view.activities

import android.app.Activity
import android.content.Intent
import com.currencytrackingapp.view.activities.main.CurrenciesActivity

class ActivityManagerImpl(val activity: Activity) : ActivityManager {

    override fun openCurrenciesActivity() {
        activity.startActivity(Intent(activity, CurrenciesActivity::class.java))
    }
}