package com.currencytrackingapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.currencytrackingapp.data.models.RatesConverter
import com.currencytrackingapp.data.models.RatesObject

/**
 * @author Tomislav Curis
 */
@Database(entities = [RatesObject::class], version = 1, exportSchema = false)
@TypeConverters(RatesConverter::class)
abstract class CurrenciesDatabase : RoomDatabase(){

    abstract fun currenciesDao(): CurrenciesDao
}