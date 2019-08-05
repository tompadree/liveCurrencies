package com.currencytrackingapp.di

import com.currencytrackingapp.data.api.NetworkApi
import com.currencytrackingapp.data.api.impl.NetworkApiImpl
import com.currencytrackingapp.BuildConfig
import com.currencytrackingapp.data.api.APIConstants.Companion.SERVER_URL
import com.currencytrackingapp.data.api.RevolutApi
import com.currencytrackingapp.utils.network.*
import com.flexfrontbv.flexfastapp.utils.network.NotAuthorizedHandler
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val NetModule = module {

//    single { Auth() }
//    single { AppConfig(get()) }

    factory { NotAuthorizedHandlerImpl(get()) as NotAuthorizedHandler }

    single {

        OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(ResponseInterceptor(get(), get())).apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    single {
        (Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(get())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(RxThreadCallAdapter(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RevolutApi::class.java)) as RevolutApi
    }
//
    single { NetworkApiImpl(get()) as NetworkApi }
    factory { InternetConnectionManagerImpl(get()) as InternetConnectionManager }
//
//    single { LoginRepositoryImpl(get(), get()) as LoginRepository }
//    single { MainRepositoryImpl(get(), get()) as MainRepository }

}