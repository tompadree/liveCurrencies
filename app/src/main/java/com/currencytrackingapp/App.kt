package com.currencytrackingapp

import androidx.multidex.MultiDexApplication
import com.currencytrackingapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

import timber.log.Timber

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(AppModule, DataModule, RepoModule, NetModule))
        }
    }
}