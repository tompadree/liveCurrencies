package com.currencytrackingapp.base

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.currencytrackingapp.R
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.currencytrackingapp.utils.network.InternetConnectionManager
import org.koin.android.ext.android.inject


class CurrenciesActivity : AppCompatActivity() {

    private val onDestinationChangedListener = this@CurrenciesActivity::onDestinationChanged
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val internetConnectionManager: InternetConnectionManager by inject()
    private lateinit var internetReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        showSnackbar(findViewById(android.R.id.content))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.mainNavHost).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    override fun onPause() {
        val navHostFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment?)!!
        navHostFragment.navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        val navHostFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment?)!!
        navHostFragment.navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    private fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?) {

    }

    private fun showSnackbar(parentLayout: View){

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        internetReceiver = internetConnectionManager.isInternetAvailable(findViewById(android.R.id.content))
        registerReceiver(internetReceiver, intentFilter)
    }
}
