package com.currencytrackingapp.di

import androidx.room.Room
import com.currencytrackingapp.data.local.LocalPrefs
import com.currencytrackingapp.data.local.LocalPrefsImpl
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.helpers.impl.AppUtilsImpl
import com.currencytrackingapp.viewmodel.CurrenciesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DataModule = module {

    single { LocalPrefsImpl(get()) as LocalPrefs }
    factory { AppUtilsImpl(get()) as AppUtils }


    viewModel { CurrenciesViewModel(get(), get()) }
}