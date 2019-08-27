package com.currencytrackingapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.currencytrackingapp.data.models.Response
import com.currencytrackingapp.data.models.ResponseError
import com.currencytrackingapp.data.models.ResponseSucces
import com.currencytrackingapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel: ViewModel(), CoroutineScope {

    protected val TAG: String = this::class.java.simpleName

    private val cancelJob = Job()
    override val coroutineContext: CoroutineContext
        get() = cancelJob + Dispatchers.Default

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error


    protected fun setLoading(loading: Boolean) {
        _loading.value = loading
    }

    protected fun postLoading(loading: Boolean) {
        _loading.postValue(loading)
    }

    protected fun <T> handleResponse(response: Response<T>): T? {
        return when (response) {
            is ResponseSucces -> response.data
            is ResponseError -> null
        }
    }

    protected fun <T> handleResponseWithError(response: Response<T>): T? {
        return when (response) {
            is ResponseSucces -> response.data
            is ResponseError -> {
                _error.postValue(response.t)
                null
            }
        }
    }

    protected open fun handleError(error: Throwable) {
        _error.postValue(error)
    }

    override fun onCleared() {
        try {
            cancelJob.cancelChildren()
            cancelJob.cancel()
        } finally {
            super.onCleared()
        }
    }
}