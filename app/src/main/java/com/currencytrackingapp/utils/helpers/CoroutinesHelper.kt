package com.currencytrackingapp.utils.helpers

import android.os.Handler
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.currencytrackingapp.data.models.Response
import com.currencytrackingapp.data.models.ResponseError
import com.currencytrackingapp.data.models.ResponseSucces
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

object RequestExecutor {

    suspend fun <T> execute(
        request: Deferred<T>,
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        try {
            val response = request.await()
            onSuccess.invoke(response)
        } catch (e: Exception) {
            onError?.invoke(e)
        }
    }

    suspend fun <T> execute(request: Deferred<T>): Response<T> {
        return try {
            val response = request.await()
            ResponseSucces(response)
        } catch (e: Exception) {
            ResponseError(e)
        }
    }
}
