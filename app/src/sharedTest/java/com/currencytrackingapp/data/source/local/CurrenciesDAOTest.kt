package com.currencytrackingapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import com.google.common.truth.Truth.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrenciesDAOTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: CurrenciesDatabase
    private lateinit var currenciesDAO: CurrenciesDao

    @Before
    fun initDB () {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), CurrenciesDatabase::class.java).build()
        currenciesDAO = database.currenciesDao()
    }

    @After
    fun closeDB() = database.close()

    private var ratesObject = RatesObject(1, "EUR", "2018-09-06", FakeRepository.dummyRates)

    @Test
    fun insertRatesObjectAndGet() = runBlockingTest {
        // Insert a ratesObject
        currenciesDAO.insertRates(ratesObject)

        // retrieve object
        val ratesObj = currenciesDAO.getCurrencies()

        assertThat(ratesObj.id).isEqualTo(ratesObject.id)
        assertThat(ratesObj.date).isEqualTo(ratesObject.date)
        assertThat(ratesObj.base).isEqualTo(ratesObject.base)
        assertThat(ratesObj.rates["EUR"]).isEqualTo(ratesObject.rates["EUR"])
    }

    @Test
    fun insertRatesReplacesOnConflict() = runBlockingTest {
        // Insert a ratesObject
        currenciesDAO.insertRates(ratesObject)

        // Insert new with the same id
        val newObj = RatesObject(1, "AUD", "2019-09-06", FakeRepository.dummyRates)
        currenciesDAO.insertRates(newObj)

        // retrieve object
        val ratesObj = currenciesDAO.getCurrencies()

        assertThat<RatesObject>(ratesObj as RatesObject, notNullValue())
        assertThat(ratesObj.id, `is`(1))
        assertThat(ratesObj.base, `is`("AUD"))
        assertThat(ratesObj.date, `is`("2019-09-06"))

    }

    @Test
    fun deleteRates() = runBlockingTest {
        // Insert a ratesObject
        currenciesDAO.insertRates(ratesObject)

        // Delete ratesObject
        currenciesDAO.deleteRates()

        // retrieve object
        val ratesObj = currenciesDAO.getCurrencies()

        assertThat(ratesObj).isNull()
    }
}