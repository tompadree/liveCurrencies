package com.currencytrackingapp.di

import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.currencytrackingapp.BuildConfig
import com.currencytrackingapp.currencies.CurrenciesViewModel
import com.currencytrackingapp.data.source.CurrenciesDataSource
import com.currencytrackingapp.data.source.remote.api.APIConstants
import com.currencytrackingapp.data.source.remote.api.RevolutApi
import com.currencytrackingapp.data.source.CurrenciesRepository
import com.currencytrackingapp.data.source.CurrenciesRepositoryImpl
import com.currencytrackingapp.data.source.local.CurrenciesDao
import com.currencytrackingapp.data.source.local.CurrenciesDatabase
import com.currencytrackingapp.data.source.local.CurrenciesLocalDataSource
import com.currencytrackingapp.data.source.local.prefs.LocalPrefs
import com.currencytrackingapp.data.source.local.prefs.LocalPrefsImpl
import com.currencytrackingapp.data.source.remote.CurrenciesRemoteDataSource
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.helpers.dialogs.DialogManager
import com.currencytrackingapp.utils.helpers.dialogs.DialogManagerImpl
import com.currencytrackingapp.utils.helpers.impl.AppUtilsImpl
import com.currencytrackingapp.utils.network.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Tomislav Curis
 */

val AppModule = module {
    factory { (activity: FragmentActivity) -> DialogManagerImpl(activity) as DialogManager }
}

val DataModule = module {

    single { LocalPrefsImpl(get()) as LocalPrefs }
    factory { AppUtilsImpl(get()) as AppUtils }

    single { Room.databaseBuilder(androidContext(), CurrenciesDatabase::class.java, "currencies_db").build() }
    single  { get<CurrenciesDatabase>().currenciesDao() }

    single { Dispatchers.IO }

    single(named("local")) { CurrenciesLocalDataSource(get(), get()) as CurrenciesDataSource }
    single(named("remote")) { CurrenciesRemoteDataSource(get()) as CurrenciesDataSource }


    viewModel { CurrenciesViewModel(get(), get()) }
}

val RepoModule = module {
    single { CurrenciesRepositoryImpl(get(qualifier = named("local")),get(qualifier = named("remote"))) as CurrenciesRepository }
}


val NetModule = module {

    single { InternetConnectionManagerImpl() as InternetConnectionManager }

    single {

        OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(NetworkExceptionInterceptor())
            .addInterceptor(ResponseInterceptor(get())).apply {
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
}