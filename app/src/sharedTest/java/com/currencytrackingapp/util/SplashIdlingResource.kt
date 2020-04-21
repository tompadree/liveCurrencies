package com.currencytrackingapp.util

import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingResource
import java.util.*
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.test.platform.app.InstrumentationRegistry
import com.currencytrackingapp.splash.SplashFragment


/**
 * @author Tomislav Curis
 */

class SplashIdlingResource : IdlingResource {
    // List of registered callbacks
    private lateinit var idlingCallbacks: IdlingResource.ResourceCallback
    // Give it a unique id to work around an Espresso bug where you cannot register/unregister
    // an idling resource with the same name.
    private val id = UUID.randomUUID().toString()
    // Holds whether isIdle was called and the result was false. We track this to avoid calling
    // onTransitionToIdle callbacks if Espresso never thought we were idle in the first place.
//    private var isIdle = false

    lateinit var activity: FragmentActivity

    override fun getName() = "Splash $id"

    override fun isIdleNow(): Boolean {

        if(getSplashFrag() == null) return false

        val isIdle = getSplashFrag() != null

        if (isIdle) {
            idlingCallbacks.onTransitionToIdle();
        }

        return isIdle
    }


    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.idlingCallbacks = resourceCallback
    }

    private fun getSplashFrag(): SplashFragment? {
        val fragments = (activity as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments

        val childrenFrags = fragments?.flatMap { it.childFragmentManager.fragments }
            ?.mapNotNull { it } ?: emptyList()


        for(frag in childrenFrags)
            if(frag is SplashFragment)
                return frag

        return null

    }

}

/**
 * Sets the activity from an [ActivityScenario] to be used from [SplashIdlingResource].
 */
fun SplashIdlingResource.monitorActivity(
    activityScenario: ActivityScenario<out FragmentActivity>
) {
    activityScenario.onActivity {
        this.activity = it
    }
}
