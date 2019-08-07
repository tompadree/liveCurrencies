package com.currencytrackingapp.utils.helpers

import android.app.Activity
import android.content.res.Configuration
import android.location.Location
import android.view.View

interface AppUtils {

    fun getConfiguration(): Configuration

    fun getColor(id: Int): Int

    fun getString(strId: Int): String

    fun toast(mes: Any)

    fun toast(id: Int)

    fun hideKeyboard(activity: Activity)

    fun showKeyboard(activity: Activity)
}