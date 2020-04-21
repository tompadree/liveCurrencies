package com.currencytrackingapp.currencies

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.currencytrackingapp.R
import com.currencytrackingapp.TestApp
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.source.CurrenciesRepository
import com.currencytrackingapp.data.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito

/**
 * @author Tomislav Curis
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class CurrenciesFragmentTest : KoinTest {

    // Use a fake repository to be injected
    private lateinit var repository: CurrenciesRepository

    private var dummyRatesObject = RatesObject(1, "EUR", "2018-09-06", FakeRepository.dummyRates)

    private val viewModel : CurrenciesViewModel by inject()
    @Before
    fun initRepo() {

        repository = FakeRepository()

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        application.injectModule(module {
            single(override = true) { repository }
        })

        // Fill the db
        runBlocking {  repository.saveRates(dummyRatesObject) }
    }

    @After
    fun cleanUpDB(){
        runBlocking {  repository.deleteRates() }
    }

    @Test
    fun displayRates() {

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<CurrenciesFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // THEN - Verify rate is displayed on screen
        onView(withText("AUD")).check(matches(isDisplayed()))
        onView(withText("CAD")).check(matches(isDisplayed()))
        onView(withText("BGN")).check(matches(isDisplayed()))
        onView(withText("DKK")).check(matches(isDisplayed()))
        onView(withText("GBP")).check(matches(isDisplayed()))
    }


    @Test
    fun displaySavedRates() {
        // WHEN Add fake rates
        runBlocking {  repository.saveRates(RatesObject(1, "TEST", "2018-09-06", hashMapOf("TEST" to 100.00))) }

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<CurrenciesFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // THEN - Verify rate is displayed on screen
        onView(withText("TEST")).check(matches(isDisplayed()))
    }

    @Test
    fun onValueChange() {

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<CurrenciesFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Calculated value of EUR to BRL conversion
        val currentRate = (viewModel.roundOffDecimal(100.00 * dummyRatesObject.rates["BRL"]!!)).toString()

        // WHEN - Check change value of the first item
        onView(editTextWithTextView("BRL")).check(matches(withText(currentRate)))
    }

    @Test
    fun clickOnItemSetCheckFirst() {

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<CurrenciesFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the CNY list item
        onView(withId(R.id.currenciesRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                   hasDescendant(withText("CNY")), click()))

        // THEN verify CNY is the first item
        onView(withText("CNY")).check(matches(
            customMatcherForFirstItem("CNY", withParent(withId(R.id.currenciesRv)))))

        // Calculated value of EUR to CNY conversion
        val currentRate = (viewModel.roundOffDecimal(100.00 * dummyRatesObject.rates["CNY"]!!)).toString()

        // WHEN - Check change value of the first item
        onView(editTextWithTextView("CNY")).check(matches(withText(currentRate)))

    }

    @Test
    fun clickOnItemCheckValues() {

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<CurrenciesFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the CNY list item
        onView(withId(R.id.currenciesRv))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("CZK")), click()))

        // THEN verify CZK is the first item
        onView(withText("CZK")).check(matches(
            customMatcherForFirstItem("CZK", withParent(withId(R.id.currenciesRv)))))

        // Calculated value of EUR to CZK conversion
        val currentRateFirst = (viewModel.roundOffDecimal(100.00 * dummyRatesObject.rates["CZK"]!!)).toString()

        // WHEN - Check change value of the first item
        onView(editTextWithTextView("CZK")).check(matches(withText(currentRateFirst)))

        // Calculated value of EUR to CHF conversion
        val currentRateOther = (viewModel.roundOffDecimal(currentRateFirst.toDouble() * dummyRatesObject.rates["CHF"]!!)).toString()

        // WHEN - Change value of the first item
        onView(editTextWithTextView("CHF")).check(matches(withText(currentRateOther)))
    }

    @Test
    fun checkOtherValuesOnFirstValueEdited() {
        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<CurrenciesFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Change  the value in the first item EditText (init is 100.0)
        onView(withText("100.0")).perform(replaceText("145.00"))

        // Calculated value of EUR to DKK conversion
        val currentRate = (viewModel.roundOffDecimal(145.00 * dummyRatesObject.rates["DKK"]!!)).toString()

        // WHEN - Check change value of the first item
        onView(editTextWithTextView("DKK")).check(matches(withText(currentRate)))
    }

    // True if first item in the RV and input text
    private fun <T> customMatcherForFirstItem(name: String, matcher: Matcher<T>): Matcher<T> {
        return object: BaseMatcher<T> () {
            var isFirst = true
            override fun matches(item: Any): Boolean {
                if (isFirst && (item as TextView).text == name) {
                    isFirst = false
                    return true
                }
                return false
            }
            override fun describeTo(description: Description?) {}
        }
    }

    // Matcher for EditText by checking TextView title
    private fun editTextWithTextView(text: String): Matcher<View> {
        return CoreMatchers.allOf(withId(R.id.itemCurrenciesEtAmount), hasSibling(withText(text)))
    }
}