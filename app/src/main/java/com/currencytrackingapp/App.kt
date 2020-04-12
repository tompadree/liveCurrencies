package com.currencytrackingapp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.currencytrackingapp.di.AppModule
import com.currencytrackingapp.di.DataModule
import com.currencytrackingapp.di.NetModule
import org.koin.core.KoinComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

import timber.log.Timber

class App : MultiDexApplication() { //, KoinComponent {

    private val lifecycleListener = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onMoveToForeground() {
            Timber.d("Returning to foreground…")
            isAppForeground = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onMoveToBackground() {
            Timber.d("Moving to background…")
            isAppForeground = false
        }
    }


    override fun onCreate() {
        super.onCreate()
        initKoin()
        setupLifecycleListener()
    }

    private fun setupLifecycleListener() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleListener)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(AppModule, DataModule, NetModule))
        }
    }

    companion object {

        private var isAppForeground = false

        fun isAppForeground(): Boolean = isAppForeground
    }
}