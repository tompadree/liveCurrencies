package com.currencytrackingapp.utils.helpers

import android.os.Handler
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.currencytrackingapp.utils.SingleLiveEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Deferred

fun <T> LiveData<T>.observe(owner: LifecycleOwner, f: (T?) -> Unit) {
    observe(owner, Observer {
        f(it)
    })
}

fun delay(t: Long = 300, f: () -> Unit) {
    Handler().postDelayed({
        f()
    }, t)
}

// Snackbar
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        show()
    }
}

fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<SingleLiveEvent<Int>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}
