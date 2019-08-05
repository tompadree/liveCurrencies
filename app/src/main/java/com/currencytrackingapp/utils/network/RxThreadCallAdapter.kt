package com.currencytrackingapp.utils.network

import io.reactivex.Flowable
import io.reactivex.Scheduler
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxThreadCallAdapter(private val subscribeScheduler: Scheduler) : CallAdapter.Factory() {

    private var rxFactory = RxJava2CallAdapterFactory.create()

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<Flowable<*>, Flowable<*>>? {
        return try {
            val adapter = rxFactory.get(returnType, annotations, retrofit) ?: return null
            val callAdapter = adapter as CallAdapter<Flowable<*>, Flowable<*>>
            ThreadCallAdapter(callAdapter)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    internal inner class ThreadCallAdapter(
            private var delegateAdapter: CallAdapter<Flowable<*>, Flowable<*>>
    ) : CallAdapter<Flowable<*>, Flowable<*>> {

        override fun responseType(): Type {
            return delegateAdapter.responseType()
        }


        override fun adapt(call: Call<Flowable<*>>): Flowable<*> {
            return delegateAdapter.adapt(call)
                    .subscribeOn(subscribeScheduler)
        }
    }
}