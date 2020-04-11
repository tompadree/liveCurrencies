package com.currencytrackingapp.di

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.currencytrackingapp.view.activities.ActivityManager
import com.currencytrackingapp.view.activities.ActivityManagerImpl
import com.currencytrackingapp.view.dialogs.DialogManager
import com.currencytrackingapp.view.dialogs.DialogManagerImpl
import org.koin.dsl.module

val AppModule = module {
    factory { (activity: Activity) -> ActivityManagerImpl(activity) as ActivityManager }
    factory { (activity: FragmentActivity) -> DialogManagerImpl(activity) as DialogManager }
}