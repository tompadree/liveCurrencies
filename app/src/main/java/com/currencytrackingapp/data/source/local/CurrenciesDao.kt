package com.currencytrackingapp.data.source.local

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.currencytrackingapp.data.models.RatesObject

/**
 * @author Tomislav Curis
 */
@Dao
interface CurrenciesDao {

    /**
     * Observes list of rates.
     *
     * @return all rates.
     */
    @Query("SELECT * FROM ratesObject")
    @Nullable
    fun observeRates(): LiveData<RatesObject>

    /**
     * Delete all rates.
     */
    @Query("DELETE FROM ratesObject")
    suspend fun deleteRates()


    /**
     * Select all rates from the rates table.
     *
     * @return all rates.
     */
    @Query("SELECT * FROM ratesObject ")
    suspend fun getCurrencies(): RatesObject


    /**
     * Insert a rates in the database. If the rates already exists, replace it.
     *
     * @param rates the rates to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: RatesObject)
}