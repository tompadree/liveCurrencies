package com.currencytrackingapp.currencies

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.currencytrackingapp.MainCoroutineRule
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.data.source.FakeRepository
import com.currencytrackingapp.utils.network.InternetConnectionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.di.AppModule
import com.currencytrackingapp.di.DataModule
import com.currencytrackingapp.di.NetModule
import com.currencytrackingapp.getOrAwaitValue
import com.currencytrackingapp.observeForTesting
import com.currencytrackingapp.utils.SingleLiveEvent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.mockito.Mock

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
//@RunWith(MockitoJUnitRunner::class)
class CurrenciesViewModelTest : KoinTest{

    // What is testing
    private lateinit var currenciesViewModel: CurrenciesViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeRepository

    // Context for internet manager
    @Mock
    private lateinit var context: Application

    // Rule for koin injection
    @get:Rule
    val koinTestRule = KoinTestRule.create {
//        MockitoAnnotations.initMocks(Context::class.java)
//        androidContext(context)
        modules(listOf(AppModule, DataModule, NetModule))
    }

    private val internetConnectionManager: InternetConnectionManager by inject()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        repository = FakeRepository()

        currenciesViewModel = CurrenciesViewModel(repository, internetConnectionManager)
    }

    @Test
    fun fetchingRatesToView() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // StartFetching
        currenciesViewModel.refresh(true)

        // Observe the items to keep LiveData emitting
        currenciesViewModel.items.observeForTesting {

            // Loding
            assertThat(currenciesViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // loading is done
            assertThat(currenciesViewModel.dataLoading.getOrAwaitValue()).isFalse()

            // Size of rates is 33
            assertThat(currenciesViewModel.items.value!!.size).isEqualTo(33)

            // First item on showing up is EUR
            assertThat(currenciesViewModel.items.value!![0].name).isEqualTo("EUR")
        }
    }

    @Test
    fun fetchingRatesGetError() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Set repo return error
        repository.setReturnError(true)

        // StartFetching
        currenciesViewModel.refresh(true)

        // Observe the items to keep LiveData emitting
        currenciesViewModel.items.observeForTesting {

            // Loding
            assertThat(currenciesViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // loading is done
            assertThat(currenciesViewModel.dataLoading.getOrAwaitValue()).isFalse()

            // If isDataLoadingError response was error
            assertThat(currenciesViewModel.isDataLoadingError.value).isEqualTo(true)
        }
    }

    @Test
    fun onClickListChanged() {

        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        runBlockingTest {
            currenciesViewModel._currentBase.set("CNY")
            currenciesViewModel._currentValue.set("106.05")
            repository.saveRates(RatesObject(1, "", "",
                    hashMapOf(
                        "CNY" to 7.9518,
                        "AUD" to 0.20338,
                        "BGN" to 0.24609,
                        "BRL" to 0.60293,
                        "CAD" to 0.193,
                        "CHF" to 0.14187
                    )
                )
            )
        }

        // Observe the items to keep LiveData emitting after list is changed
        currenciesViewModel.items.observeForTesting {

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // Size of rates is now 7
            assertThat(currenciesViewModel.items.value!!.size).isEqualTo(6)

            // First item showing up after click/change in the list is CNY
            assertThat(currenciesViewModel.items.value!![0].name).isEqualTo("CNY")

            // Items changed value, AUD in this case
            assertThat(currenciesViewModel.items.value!![1].currentRate).isEqualTo(currenciesViewModel.roundOffDecimal(106.05*0.20338))
        }
    }
}
