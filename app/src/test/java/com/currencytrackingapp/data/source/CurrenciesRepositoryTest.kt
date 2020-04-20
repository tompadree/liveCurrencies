package com.currencytrackingapp.data.source


import com.currencytrackingapp.MainCoroutineRule
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import com.currencytrackingapp.di.AppModule
import com.currencytrackingapp.di.DataModule
import com.currencytrackingapp.di.NetModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.koin.test.KoinTestRule

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
class CurrenciesRepositoryTest {

    companion object {
        // Dataset
        val dummyRates: HashMap<String, Double> =
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

    }
    private val ratesObj = RatesObject(1,"EUR","2018-09-06", dummyRates)
    private val ratesObjLocal = RatesObject(2,"AUD","2018-09-07", dummyRates)
    private val ratesObjRemote = RatesObject(3,"CAD","2018-09-08", dummyRates)

    private lateinit var currenciesLocalDataSource: FakeDataSource
    private lateinit var currenciesRemoteDataSource: FakeDataSource

    private lateinit var repository: CurrenciesRepositoryImpl

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepo() {
        currenciesLocalDataSource = FakeDataSource(ratesObjLocal)
        currenciesRemoteDataSource = FakeDataSource(ratesObjRemote)

        repository = CurrenciesRepositoryImpl(currenciesLocalDataSource, currenciesRemoteDataSource)

    }

    @Test
    fun getTasks_emptyRepositoryAndUninitializedCache() = mainCoroutineRule.runBlockingTest {
        val emptySource = FakeDataSource()
        val tasksRepository = CurrenciesRepositoryImpl(emptySource, emptySource)

        assertThat(tasksRepository.getLatestRates(true, "EUR") is Result.Success).isTrue()
    }

    @Test
    fun getRates_repoCacheAfterFirstApiCall () = mainCoroutineRule.runBlockingTest {
        // false trigger is default
        val initial = repository.getLatestRates()

        currenciesRemoteDataSource.ratesObject = ratesObj

        val second = repository.getLatestRates()

        // Initial and second should match because no backend is called
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun getRates_requestRatesFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the tasks repository
        val rates = repository.getLatestRates(true, "CAD") as Result.Success

        // Then tasks are loaded from the remote data source
        assertThat(rates.data.base, IsEqual(ratesObjRemote.base))
    }

    @Test
    fun getTasks_requestAllTasksFromLocalDataSource() = mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the tasks repository
        val rates = repository.getLatestRates(false, "AUD") as Result.Success

        // Then tasks are loaded frotm the local repository
        assertEquals(rates.data, ratesObjLocal)
    }

    @Test
    fun saveRates_toLocal() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val rates = repository.getLatestRates(true, "EUR") as Result.Success

        // Save rates
        repository.saveRates(rates.data)

        // Fetch them
        val ratesLocal = repository.getLatestRates(false, "EUR") as Result.Success

        assertThat(rates.data).isEqualTo(ratesLocal.data)
    }

    @Test
    fun getRates_remoteUnavailable() = mainCoroutineRule.runBlockingTest {
        // Remote unavailable
        currenciesRemoteDataSource.ratesObject = null

        val result = repository.getLatestRates(true, "EUR")

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getRates_remoteUnavailableGetFromLOcal() = mainCoroutineRule.runBlockingTest {
        currenciesRemoteDataSource.ratesObject = null

        assertThat((repository.getLatestRates() as Result.Success).data).isEqualTo(ratesObjLocal)
    }

    @Test
    fun getRates_BothSourcesUnavailable() = mainCoroutineRule.runBlockingTest {
        // Both sources null
        currenciesRemoteDataSource.ratesObject = null
        currenciesLocalDataSource.ratesObject = null

        assertThat(repository.getLatestRates()).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getRates_refreshRatesFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // Initial state in db
        val initial = repository.getLatestRates() as Result.Success // currenciesLocalDataSource.ratesObject

        // Fetch from remote
        val remoteRates = repository.getLatestRates(true,"EUR") as Result.Success

        assertEquals(remoteRates.data.base, ratesObjRemote.base)
        assertEquals(remoteRates.data, currenciesLocalDataSource.ratesObject)
        assertThat(currenciesLocalDataSource.ratesObject).isNotEqualTo(initial)
    }

    @Test
    fun getRates_deleteRates() = mainCoroutineRule.runBlockingTest {
        // Get rates
        val initialRates = repository.getLatestRates() as? Result.Success

        currenciesLocalDataSource.deleteRates()

        // Fetch after delete
        val afterDeleteRates = repository.getLatestRates() as? Result.Success

        //check
        assertThat(initialRates).isNotNull()
        assertThat(afterDeleteRates).isNull()
    }

}