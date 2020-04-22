package com.currencytrackingapp.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.data.models.RatesObject
import com.currencytrackingapp.data.models.Result
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class FakeDataSource(var ratesObject: RatesObject?
                     = RatesObject(1,"ZAR","2018-08-06", HashMap())
) : CurrenciesDataSource {

    override suspend fun getLatestRates(base: String): Result<RatesObject> {
        ratesObject?.let { return Result.Success(it) }
        return Result.Error(Exception("Exception"))
    }

    override suspend fun deleteRates() {
        ratesObject = null
    }

//    override suspend fun deleteRates() {
////        ratesObject = null //RatesObject(1,"","", mutableMapOf<String,Double>() as HashMap<String, Double>)
//        ratesObject?.id = null
//        ratesObject?.base = ""
//        ratesObject?.date = ""
//        ratesObject?.rates?.clear()
//    }

    override suspend fun saveRates(rates: RatesObject) {
        ratesObject?.id = rates.id
        ratesObject?.base = rates.base
        ratesObject?.date = rates.date
        ratesObject?.rates = rates.rates
    }

//    override suspend fun saveRates(rates: RatesObject) {
//        ratesObject = rates
//    }

    override fun observeRates(): LiveData<Result<RatesObject>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun refreshRates(base: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}