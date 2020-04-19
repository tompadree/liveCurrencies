package com.currencytrackingapp.data.source


import com.currencytrackingapp.MainCoroutineRule
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
class CurrenciesRepositoryTest {



    // Dataset
    private val rates: HashMap<String, Double> =
        hashMapOf(
            "AUD" to 1.6178,
            "BGN" to 1.95,
            "BRL" to 4.7958,
            "CAD" to 1.5351,
            "CHF" to 1.1285,
            "CNY" to 7.9518,
            "CZK" to 25.737,
            "DKK" to 7.463,
            "GBP" to 0.899,
            "HKD" to 9.1401,
            "HRK" to 7.4404,
            "HUF" to 326.77,
            "IDR" to 17338.00,
            "ILS" to 4.1741,
            "INR" to 83.788,
            "ISK" to 127.91,
            "JPY" to 129.66,
            "KRW" to 1305.9,
            "MXN" to 22.384,
            "MYR" to 4.8161,
            "NOK" to 9.7842,
            "NZD" to 1.7648,
            "PHP" to 62.645,
            "PLN" to 4.3219,
            "RON" to 4.6424,
            "RUB" to 79.642,
            "SEK" to 10.6,
            "SGD" to 1.6013,
            "THB" to 38.162,
            "TRY" to 7.6346,
            "USD" to 1.1644,
            "ZAR" to 17.838
        )

    private val ratesObj = RatesObject(1,"EUR","2018-09-06", rates)

    private lateinit var currenciesLocalDataSource: FakeDataSource
    private lateinit var currenciesRemoteDataSource: FakeDataSource

    private lateinit var repository: CurrenciesRepositoryImpl

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepo() {
        currenciesLocalDataSource = FakeDataSource(ratesObj)
        currenciesRemoteDataSource = FakeDataSource(ratesObj)

        repository = CurrenciesRepositoryImpl(currenciesLocalDataSource, currenciesRemoteDataSource)

    }

    @Test
    fun getTasks_emptyRepositoryAndUninitializedCache() = mainCoroutineRule.runBlockingTest {
        val emptySource = FakeDataSource()
        val tasksRepository = CurrenciesRepositoryImpl(emptySource, emptySource)

        assertThat(repository.getLatestRates(true, "EUR") is Result.Success).isTrue()
    }


    @Test
    fun getRates_requestRatesFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the tasks repository
        val rates = repository.getLatestRates(false, "EUR") as Result.Success

        // Then tasks are loaded from the remote data source
        assertThat(rates.data, IsEqual(ratesObj))

    }

    @Test
    fun getTasks_requestAllTasksFromLocalDataSource() = mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the tasks repository
        val rates = repository.getLatestRates(true, "EUR") as Result.Success

        // Then tasks are loaded frotm the local repository
        assertEquals(rates.data, ratesObj)
    }


}