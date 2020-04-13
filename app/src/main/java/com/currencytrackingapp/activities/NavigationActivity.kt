package com.currencytrackingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.currencytrackingapp.R
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView


class NavigationActivity : BaseActivity() {

    private val onDestinationChangedListener = this@NavigationActivity::onDestinationChanged
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val navController: NavController = findNavController(R.id.mainNavHost)
//        appBarConfiguration =
//            AppBarConfiguration.Builder(R.id.tasks_fragment_dest, R.id.statistics_fragment_dest)
//                .setDrawerLayout(drawerLayout)
//                .build()
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        findViewById<NavigationView>(R.id.nav_view)
//            .setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.mainNavHost).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

//    override fun onPause() {
//        val navHostFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment?)!!
//        navHostFragment.navController.removeOnDestinationChangedListener(onDestinationChangedListener)
//        super.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val navHostFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment?)!!
//        navHostFragment.navController.addOnDestinationChangedListener(onDestinationChangedListener)
//    }

    private fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?) {

    }
}
