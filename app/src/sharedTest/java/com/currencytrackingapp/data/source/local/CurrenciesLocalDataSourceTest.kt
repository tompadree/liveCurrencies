package com.currencytrackingapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.currencytrackingapp.data.source.CurrenciesDataSource
import androidx.test.filters.MediumTest
import com.currencytrackingapp.util.MainCoroutineRule
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.data.source.FakeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import com.google.common.truth.Truth.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue

/**
 * @author Tomislav Curis
 *
 * Integration test for the [CurrenciesDataSource].
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class CurrenciesLocalDataSourceTest {

    private lateinit var localDataSource: CurrenciesLocalDataSource
    private lateinit var database: CurrenciesDatabase

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), CurrenciesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        localDataSource = CurrenciesLocalDataSource(database.currenciesDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    private var ratesObject = RatesObject(1, "EUR", "2018-09-06", FakeRepository.dummyRates)


    @Test
    fun insertRatesObjectAndGet() = runBlockingTest {
        // Insert a ratesObject
        localDataSource.saveRates(ratesObject)

        // retrieve object
        val ratesObj = localDataSource.getLatestRates("") as Result.Success

        assertThat(ratesObj.data.id).isEqualTo(ratesObject.id)
        assertThat(ratesObj.data.date).isEqualTo(ratesObject.date)
        assertThat(ratesObj.data.base).isEqualTo(ratesObject.base)
        assertThat(ratesObj.data.rates["EUR"]).isEqualTo(ratesObject.rates["EUR"])
    }

    @Test
    fun insertRatesReplacesOnConflict() = runBlockingTest {
        // Insert a ratesObject
        localDataSource.saveRates(ratesObject)

        // Insert new with the same id
        val newObj = RatesObject(1, "AUD", "2019-09-06", FakeRepository.dummyRates)
        localDataSource.saveRates(newObj)

        // retrieve object
        val ratesObj = localDataSource.getLatestRates("") as Result.Success

        assertThat<RatesObject>(ratesObj.data as RatesObject, notNullValue())
        assertThat(ratesObj.data.id, `is`(1))
        assertThat(ratesObj.data.base, `is`("AUD"))
        assertThat(ratesObj.data.date, `is`("2019-09-06"))

    }

    @Test
    fun deleteRates() = runBlockingTest {
        // Insert a ratesObject
        localDataSource.saveRates(ratesObject)

        // Delete ratesObject
        localDataSource.deleteRates()

        // retrieve object
        val ratesObj = localDataSource.getLatestRates("") as Result.Success

        assertThat(ratesObj.data).isNull()
    }

}