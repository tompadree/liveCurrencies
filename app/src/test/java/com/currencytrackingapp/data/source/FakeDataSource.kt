package com.currencytrackingapp.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class FakeDataSource(var ratesObject: RatesObject?
                     = RatesObject(1,"EUR","2018-09-06", HashMap<String, Double>())
) : CurrenciesDataSource {

    override fun observeRates(): LiveData<Result<RatesObject>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLatestRates(base: String): Result<RatesObject> {
        ratesObject?.let { return Result.Success(it) }
        return Result.Error(Exception("Exception"))
    }

    override suspend fun refreshRates(base: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveRates(rates: RatesObject) {
        ratesObject = rates
    }
}