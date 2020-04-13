package com.currencytrackingapp.fragments
//
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.LiveData
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//
//import com.flexfrontbv.flexfastapp.R
//import com.flexfrontbv.flexfastapp.data.local.Auth
//import com.flexfrontbv.flexfastapp.utils.helpers.AppUtils
//import com.flexfrontbv.flexfastapp.utils.network.NetworkException
//import com.flexfrontbv.flexfastapp.utils.observe
//import com.flexfrontbv.flexfastapp.view.dialogs.LoadingDialog
//import com.flexfrontbv.flexfastapp.utils.network.InternetConnectionException
//import com.flexfrontbv.flexfastapp.view.interfaces.DialogManager
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import org.koin.core.parameter.parametersOf
//import org.koin.standalone.KoinComponent
//import org.koin.standalone.get
//import org.koin.standalone.inject
//import kotlin.coroutines.CoroutineContext
//
//
//abstract class BaseFragment : Fragment(), CoroutineScope, KoinComponent {
//
//    protected val TAG = this::class.java.simpleName
//
//    private var dialogManager: DialogManager? = null
//
//    val auth: Auth by inject { parametersOf(this) }
//    val appUtils: AppUtils by inject { parametersOf(this) }
//
//    private val fragmentContextJob = Job()
//    override val coroutineContext: CoroutineContext
//        get() = fragmentContextJob + Dispatchers.Main
//
//    private var loadingDialog: LoadingDialog? = null
//
//
//    override fun onStop() {
//        super.onStop()
//        dialogManager?.dismissAll()
//    }
//
//    override fun onDestroy() {
//        try {
//            loadingDialog?.dismiss()
//            fragmentContextJob.cancel()
//        } finally {
//            super.onDestroy()
//        }
//    }
//
//    protected open fun observeLoading(loadingLiveData: LiveData<Boolean>) {
//        loadingLiveData.observe(this) {
//            setLoading(it == true)
//        }
//    }
//
//    protected open fun observeError(errorLiveData: LiveData<Throwable>) {
//        errorLiveData.observe(this) {
//            showError(it ?: return@observe)
//        }
//    }
//
//    protected open fun observeErrorRefreshLayout(errorLiveData: LiveData<Throwable>, swipeRefreshLayout: SwipeRefreshLayout) {
//        errorLiveData.observe(this) {
//            showError(it ?: return@observe)
//            swipeRefreshLayout.isRefreshing = false
//        }
//    }
//
//    protected open fun setLoading(loading: Boolean) {
//        if (loading) showLoading() else hideLoading()
//    }
//
//    protected open fun showError(throwable: Throwable) {
//        if (throwable is InternetConnectionException) {
//            showError(getString(R.string.no_internet_connection_title), getString(R.string.no_internet_connection_text))
//        } else if (throwable.message == getString(R.string.bad_credentials)) {
//            showError(getString(R.string.user_pass_wrong))
//        } else if (throwable.message == getString(R.string.current_user_is_not_advisor)) {
//            showError(getString(R.string.user_is_not_advisor))
//        } else if (throwable is NetworkException) {
//            showError(throwable.getErrors()?.message)
//        } else {
//            showUnknownError()
//        }
//    }
//
//    protected open fun showError(error: String?) {
//        if (error != null) {
//            getDialogManager().openOneButtonDialog(R.string.ok, error, true)
//        } else {
//            showUnknownError()
//        }
//    }
//
//    protected open fun showError(errorTitle: String, errorMessage: String) {
//        getDialogManager().openOneButtonDialog(R.string.ok, errorTitle, errorMessage, true)
//    }
//
//    protected open fun showUnknownError() {
//        getDialogManager().openOneButtonDialog(R.string.ok, R.string.error_default, true)
//    }
//
//    fun getDialogManager(): DialogManager {
//        if (dialogManager == null) {
//            dialogManager = get { parametersOf(activity!!) }
//        }
//
//        return dialogManager!!
//    }
//
//    fun removeDialogs() {
//        dialogManager?.dismissAll()
//    }
//
//    private fun showLoading() {
//        loadingDialog?.let {
//            if (it.isShowing) return
//            it.show()
//        } ?: run {
//            loadingDialog = LoadingDialog(context!!)
//            loadingDialog?.show()
//        }
//    }
//
//    private fun hideLoading() {
//        loadingDialog?.dismiss()
//    }
//
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_base, container, false)
////    }
//
//
//}
