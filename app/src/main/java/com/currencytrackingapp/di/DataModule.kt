package com.currencytrackingapp.di

import androidx.room.Room
import com.currencytrackingapp.data.local.LocalPrefs
import com.currencytrackingapp.data.local.LocalPrefsImpl
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.helpers.impl.AppUtilsImpl

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val DataModule = module {

//    single { Auth() }
    single { LocalPrefsImpl(get()) as LocalPrefs }
    factory { AppUtilsImpl(get()) as AppUtils }

//    single    { Room.databaseBuilder(androidContext(), FlexFastDatabase::class.java, "ff_db").build() }
//
//    single { get<FlexFastDatabase>().getFlexFastDao() }
//
//    single { DossiersRepositoryImpl(get(), get()) as DossiersRepository }
//
//    single { PictureHolder(get()) }
}