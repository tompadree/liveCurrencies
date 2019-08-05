package com.currencytrackingapp.di

import android.app.Activity
import com.currencytrackingapp.view.activities.ActivityManager
import com.currencytrackingapp.view.activities.ActivityManagerImpl
import org.koin.dsl.module.module

val AppModule = module {

//    factory { (containerId: Int, fm: androidx.fragment.app.FragmentManager) ->
//        FragmentManagerImpl(containerId, fm) as FragmentManager
//    }
    factory { (activity: Activity) -> ActivityManagerImpl(activity) as ActivityManager }
//    factory { (activity: FragmentActivity) -> DialogManagerImpl(activity) as DialogManager}

//    factory { (fm: androidx.fragment.app.FragmentManager) -> DossiersBerichtenPageAdapter(fm)}

}