package com.currencytrackingapp.di

import androidx.room.Room
import com.currencytrackingapp.BuildConfig
import com.currencytrackingapp.currencies.CurrenciesViewModel
import com.currencytrackingapp.data.api.APIConstants
import com.currencytrackingapp.data.api.NetworkApi
import com.currencytrackingapp.data.api.RevolutApi
import com.currencytrackingapp.data.api.impl.NetworkApiImpl
import com.currencytrackingapp.data.source.CurrenciesRepository
import com.currencytrackingapp.data.source.CurrenciesRepositoryImpl
import com.currencytrackingapp.data.source.local.CurrenciesDatabase
import com.currencytrackingapp.data.source.local.CurrenciesLocalDataSource
import com.currencytrackingapp.data.source.local.prefs.LocalPrefs
import com.currencytrackingapp.data.source.local.prefs.LocalPrefsImpl
import com.currencytrackingapp.data.source.remote.CurrenciesRemoteDataSource
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.helpers.impl.AppUtilsImpl
import com.currencytrackingapp.utils.network.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Tomislav Curis
 */

val AppModule = module {
    //    factory { (activity: Activity) -> ActivityManagerImpl(activity) as ActivityManager }
//    factory { (activity: FragmentActivity) -> DialogManagerImpl(activity) as DialogManager }
}

val DataModule = module {

    single { LocalPrefsImpl(get()) as LocalPrefs }
    factory { AppUtilsImpl(get()) as AppUtils }

    single { Room.databaseBuilder(androidContext(), CurrenciesDatabase::class.java, "currencies_db").build() }
    single { get<CurrenciesDatabase>().currenciesDao() }

    single { CurrenciesLocalDataSource(get()) }
    single { CurrenciesRemoteDataSource(get()) }

    single { CurrenciesRepositoryImpl(get(), get()) as CurrenciesRepository }

    viewModel { CurrenciesViewModel(get()) }
}


val NetModule = module {

    factory { NotAuthorizedHandlerImpl(get()) as NotAuthorizedHandler }

    single {

        OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(ResponseInterceptor(get(), get())).apply {
                if (BuildConfig.DEBUG) {
                    var loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    single {
        (Retrofit.Builder()
            .baseUrl(APIConstants.SERVER_URL)
            .client(get())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(RxThreadCallAdapter(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RevolutApi::class.java)) as RevolutApi
    }

    single { NetworkApiImpl(get()) as NetworkApi }
    factory { InternetConnectionManagerImpl(get()) as InternetConnectionManager }

}