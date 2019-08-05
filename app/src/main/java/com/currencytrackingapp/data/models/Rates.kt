package com.currencytrackingapp.data.models

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

data class Rates (

    @SerializedName("base")
    var base: String = "",

    @SerializedName("date")
    var date: String = "",

    @TypeConverters(RatesConverter::class)
    @SerializedName("rates")
    var rates: HashMap<String, Double>,

    var ratesSorted: ArrayList<RatesListItem>

)

data class RatesListItem(

    var name: String = "",

    var currentRate: Double = 1.0
)

class RatesConverter {
    val gson = Gson()

    @TypeConverter
    fun stringToRates(value: String): HashMap<String, String> =
        gson.fromJson(value, object : TypeToken<HashMap<String, String>>() {}.type)


    @TypeConverter
    fun fromRatesToString(levels: HashMap<String, String>) = Gson().toJson(levels)

}