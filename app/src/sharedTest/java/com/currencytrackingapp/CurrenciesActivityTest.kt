package com.currencytrackingapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.currencytrackingapp.base.CurrenciesActivity
import com.currencytrackingapp.currencies.CurrenciesViewModel
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.source.CurrenciesRepository
import com.currencytrackingapp.data.source.FakeRepository
import com.currencytrackingapp.util.DataBindingIdlingResource
import com.currencytrackingapp.util.monitorActivity
import com.currencytrackingapp.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * @author Tomislav Curis
 *
 * Large End-to-End test.
 *
 * UI tests usually use [ActivityTestRule] but there's no API to perform an action before
 * each test. The workaround is to use `ActivityScenario.launch()` and `ActivityScenario.close()`.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@ExperimentalCoroutinesApi
class CurrenciesActivityTest : KoinTest {

    // Use a fake repository to be injected
    private lateinit var repository: CurrenciesRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val  dataBindingIdlingResource = DataBindingIdlingResource()

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

    // Register IdlingResource
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }


    //Unregister Idling Resource so it can be garbage collected and does not leak any memory.
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun displayRates() {

        // Start up CurrenciesActivity screen and start monitor
        val activityScenario = ActivityScenario.launch(CurrenciesActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - Verify rates are displayed on screen
        Espresso.onView(withText("AUD")).check(matches(isDisplayed()))
        Espresso.onView(withText("CAD")).check(matches(isDisplayed()))
        Espresso.onView(withText("BGN")).check(matches(isDisplayed()))
        Espresso.onView(withText("DKK")).check(matches(isDisplayed()))
        Espresso.onView(withText("GBP")).check(matches(isDisplayed()))

        // Close the activity before closing the DB
        activityScenario.close()
    }


    @Test
    fun displaySavedRates() {

        // WHEN Add fake rates
        runBlocking {  repository.saveRates(RatesObject(1, "TEST", "2018-09-06", hashMapOf("TEST" to 100.00))) }

        // Start up CurrenciesActivity screen and start monitor
        val activityScenario = ActivityScenario.launch(CurrenciesActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - Verify rate is displayed on screen
        Espresso.onView(withText("TEST")).check(matches(isDisplayed()))

        // Close the activity before closing the DB
        activityScenario.close()
    }

    @Test
    fun onValueChange() {

        // Start up CurrenciesActivity screen and start monitor
        val activityScenario = ActivityScenario.launch(CurrenciesActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Calculated value of EUR to BRL conversion
        val currentRate = (viewModel.roundOffDecimal(100.00 * dummyRatesObject.rates["BRL"]!!)).toString()

        // WHEN - Check change value of the first item
        Espresso.onView(editTextWithTextView("BRL")).check(matches(withText(currentRate)))

        // Close the activity before closing the DB
        activityScenario.close()
    }

    @Test
    fun clickOnItemSetCheckFirst() {

        // Start up CurrenciesActivity screen and start monitor
        val activityScenario = ActivityScenario.launch(CurrenciesActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - Click on the CNY list item
        Espresso.onView(withId(R.id.currenciesRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("CNY")), click()
                ))

        // THEN verify CNY is the first item
        Espresso.onView(withText("CNY")).check(
            matches(
                customMatcherForFirstItem("CNY")))

        // Calculated value of EUR to CNY conversion
        val currentRate = (
                viewModel.roundOffDecimal(100.00 * dummyRatesObject.rates["CNY"]!!)).toString()

        // WHEN - Check change value of the first item
        Espresso.onView(editTextWithTextView("CNY")).check(matches(withText(currentRate)))

        // Close the activity before closing the DB
        activityScenario.close()
    }

    @Test
    fun clickOnItemCheckValues() {

        // Start up CurrenciesActivity screen and start monitor
        val activityScenario = ActivityScenario.launch(CurrenciesActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - Click on the CNY list item
        Espresso.onView(withId(R.id.currenciesRv))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("CZK")), click()))

        // THEN verify CZK is the first item
        Espresso.onView(withText("CZK")).check(matches(
            customMatcherForFirstItem("CZK")))

        // Calculated value of EUR to CZK conversion
        val currentRateFirst = (viewModel.roundOffDecimal(100.00 * dummyRatesObject.rates["CZK"]!!)).toString()

        // WHEN - Check change value of the first item
        Espresso.onView(editTextWithTextView("CZK")).check(matches(withText(currentRateFirst)))

        // Calculated value of EUR to CHF conversion
        val currentRateOther = (viewModel.roundOffDecimal(currentRateFirst.toDouble() * dummyRatesObject.rates["CHF"]!!)).toString()

        // WHEN - Change value of the first item
        Espresso.onView(editTextWithTextView("CHF")).check(matches(withText(currentRateOther)))

        // Close the activity before closing the DB
        activityScenario.close()
    }

    @Test
    fun checkOtherValuesOnFirstValueEdited() {

        // Start up CurrenciesActivity screen and start monitor
        val activityScenario = ActivityScenario.launch(CurrenciesActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Change  the value in the first item EditText (init is 100.0)
        Espresso.onView(withText("100.0")).perform(replaceText("145.00"))

        // Calculated value of EUR to DKK conversion
        val currentRate = (viewModel.roundOffDecimal(145.00 * dummyRatesObject.rates["DKK"]!!)).toString()

        // WHEN - Check change value of the first item
        Espresso.onView(editTextWithTextView("DKK")).check(matches(withText(currentRate)))

        // Close the activity before closing the DB
        activityScenario.close()
    }

    // True if first item in the RV and input text
    private fun <T> customMatcherForFirstItem(name: String): Matcher<T> {
        return object: BaseMatcher<T>() {
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
        return CoreMatchers.allOf(
            withId(R.id.itemCurrenciesEtAmount),
            hasSibling(withText(text))
        )
    }
}