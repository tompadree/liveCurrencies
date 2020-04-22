package com.currencytrackingapp.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.currencytrackingapp.R
import com.currencytrackingapp.utils.helpers.dialogs.DialogManager
import com.currencytrackingapp.utils.network.InternetConnectionException
import com.currencytrackingapp.utils.network.NetworkException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext
import com.currencytrackingapp.utils.helpers.observe
import org.koin.android.ext.android.get


abstract class BaseFragment : Fragment(), CoroutineScope {

    protected val TAG = this::class.java.simpleName

    private var dialogManager: DialogManager? = null

    private val fragmentContextJob = Job()
    override val coroutineContext: CoroutineContext
        get() = fragmentContextJob + Dispatchers.Main



    override fun onStop() {
        super.onStop()
        dialogManager?.dismissAll()
    }

    override fun onDestroy() {
        try {
            fragmentContextJob.cancel()
        } finally {
            super.onDestroy()
        }
    }

    protected open fun observeError(errorLiveData: LiveData<Throwable>) {
        errorLiveData.observe(this) {
            showError(it ?: return@observe)
        }
    }

    protected open fun observeErrorRefreshLayout(errorLiveData: LiveData<Throwable>, swipeRefreshLayout: SwipeRefreshLayout) {
        errorLiveData.observe(this) {
            showError(it ?: return@observe)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    protected open fun showError(throwable: Throwable) {
        when (throwable) {
            is InternetConnectionException -> showError(getString(R.string.no_internet_connection_title), getString(R.string.no_internet_connection_text))
            is NetworkException -> showError(throwable.getErrors()?.message)
            else -> showUnknownError()
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

    private fun getDialogManager(): DialogManager {
        if (dialogManager == null) {
            dialogManager = get { parametersOf(requireContext()) }
        }

        return dialogManager!!
    }

    fun removeDialogs() {
        dialogManager?.dismissAll()
    }
}
