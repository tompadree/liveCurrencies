package com.currencytrackingapp.view.activities

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import com.currencytrackingapp.R
import com.currencytrackingapp.utils.network.InternetConnectionException
import com.currencytrackingapp.utils.network.InternetConnectionManager
import com.currencytrackingapp.utils.network.NetworkException
import com.currencytrackingapp.utils.helpers.observe
import com.currencytrackingapp.view.dialogs.DialogManager
import com.currencytrackingapp.view.dialogs.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), KoinComponent, CoroutineScope {

    private val activityContextJob = Job()
    override val coroutineContext: CoroutineContext
        get() = activityContextJob + Dispatchers.Main

    private var dialogManager: DialogManager? = null
    private var loadingDialog: LoadingDialog? = null

    private val internetConnectionManager: InternetConnectionManager by inject()
    private lateinit var internetReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetReceiver)
    }

    protected open fun observeLoading(loadingLiveData: LiveData<Boolean>) {
        loadingLiveData.observe(this) {
            setLoading(it == true)
        }
    }

    protected open fun setLoading(loading: Boolean) {
        if (loading) showLoading() else hideLoading()
    }

    protected open fun observeError(errorLiveData: LiveData<Throwable>) {
        errorLiveData.observe(this) {
            showError(it ?: return@observe)
        }
    }

    protected open fun showError(throwable: Throwable) {
        /*TODO*/
        if (throwable is InternetConnectionException) {
            showError(getString(R.string.no_internet_connection_title), getString(R.string.no_internet_connection_text))
        } else if (throwable is NetworkException) {
            showError(throwable.getErrors()?.message)
        } else {
            showUnknownError()
        }
    }

    protected open fun showError(error: String?) {
        if(!getDialogManager().isDialogShown())
            return
        if (error != null) {
            getDialogManager().openOneButtonDialog(R.string.ok, error, true) { getDialogManager().dismissAll()}
        } else {
            showUnknownError()
        }
    }

    protected open fun showError(errorTitle: String, errorMessage: String) {
        if(!getDialogManager().isDialogShown())
            getDialogManager().openOneButtonDialog(R.string.ok, errorTitle, errorMessage, true) { getDialogManager().dismissAll()}
    }

    protected open fun showUnknownError() {
        if(!getDialogManager().isDialogShown())
            getDialogManager().openOneButtonDialog(R.string.ok, R.string.error_default, true) { getDialogManager().dismissAll()}
    }

    private fun getDialogManager(): DialogManager {
        if (dialogManager == null) {
            dialogManager = get { parametersOf(this) }
        }

        return dialogManager!!
    }

    private fun showLoading() {
        loadingDialog?.let {
            if (it.isShowing) return
            it.show()
        } ?: run {
            loadingDialog = LoadingDialog(this)
            loadingDialog?.show()
        }
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
    }

    protected open fun showSnackbar(parentLayout: View){

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        internetReceiver = internetConnectionManager.isInternetAvailable(parentLayout)
        registerReceiver(internetReceiver, intentFilter)
    }
}
