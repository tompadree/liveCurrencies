package com.currencytrackingapp.view.activities.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.currencytrackingapp.R
import com.currencytrackingapp.common.AppConstants.Companion.SPLASH_DISPLAY_LENGTH
import com.currencytrackingapp.utils.helpers.delay
import com.currencytrackingapp.view.activities.ActivityManager
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SplashActivity : AppCompatActivity(), KoinComponent {

    private val activityManager: ActivityManager by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        launchMain()
    }

    private fun launchMain() {
        delay(SPLASH_DISPLAY_LENGTH) {
            activityManager.openCurrenciesActivity()
            finish()
        }
    }
}
