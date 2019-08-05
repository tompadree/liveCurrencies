package com.currencytrackingapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import com.currencytrackingapp.R
import com.currencytrackingapp.utils.network.InternetConnectionException
import com.currencytrackingapp.utils.network.NetworkException
import com.currencytrackingapp.utils.observe
import com.currencytrackingapp.view.dialogs.DialogManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), KoinComponent, CoroutineScope {

    private val activityContextJob = Job()
    override val coroutineContext: CoroutineContext
        get() = activityContextJob + Dispatchers.Main

    private var dialogManager: DialogManager? = null
//    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        if (error != null) {
            getDialogManager().openOneButtonDialog(R.string.ok, error, true)
        } else {
            showUnknownError()
        }
    }

    protected open fun showError(errorTitle: String, errorMessage: String) {
        getDialogManager().openOneButtonDialog(R.string.ok, errorTitle, errorMessage, true)
    }

    protected open fun showUnknownError() {
        getDialogManager().openOneButtonDialog(R.string.ok, R.string.error_default, true)
    }

    protected fun getDialogManager(): DialogManager {
        if (dialogManager == null) {
            dialogManager = get { parametersOf(this) }
        }

        return dialogManager!!
    }

    /*TODO*/
    private fun showLoading() {
//        loadingDialog?.let {
//            if (it.isShowing) return
//            it.show()
//        } ?: run {
//            loadingDialog = LoadingDialog(this)
//            loadingDialog?.show()
//        }
    }

    private fun hideLoading() {
//        loadingDialog?.dismiss()
    }
}
