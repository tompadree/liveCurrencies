package com.currencytrackingapp.utils.helpers.impl

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.InputMethod.SHOW_FORCED
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.currencytrackingapp.utils.helpers.AppUtils

class AppUtilsImpl (val context: Context) : AppUtils {

    override fun toast(mes: Any) {
        Toast.makeText(context, mes.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun toast(@StringRes id: Int) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show()
    }

    override fun getConfiguration(): Configuration = context.resources.configuration

    override fun getColor(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)

    override fun getString(@StringRes strId: Int): String = context.resources.getString(strId)


    override fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus

        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view?.windowToken ?: return, 0)
    }

    override fun showKeyboard(activity: Activity) {
        val view = activity.currentFocus

        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(view, SHOW_FORCED)
    }
}