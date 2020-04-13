package com.currencytrackingapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.RatesListItem

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
    fun observeRates(): LiveData<RatesObject>

//    @Query("SELECT rates FROM ratesObject")
//    fun observeRates(): LiveData<HashMap<String, Double>>

    /**
     * Select all rates from the rates table.
     *
     * @return all rates.
     */
    @Query("SELECT * FROM ratesObject ") //WHERE meterId=:meterId AND year=:year AND billGroupId=:billGroupId")
    suspend fun getCurrencies(): RatesObject


    /**
     * Insert a rates in the database. If the rates already exists, replace it.
     *
     * @param rates the rates to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: RatesObject)
}