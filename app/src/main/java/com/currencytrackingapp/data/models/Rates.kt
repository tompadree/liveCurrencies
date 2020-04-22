package com.currencytrackingapp.data.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "ratesObject")
class RatesObject constructor(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = 1,

    @SerializedName("base")
    var base: String = "",

    @SerializedName("date")
    var date: String = "",

    @ColumnInfo(name = "rates")
    @TypeConverters(RatesConverter::class)
    @SerializedName("rates")
    var rates: HashMap<String, Double>

)

data class RatesListItem(

    var name: String = "",

    var currentRate: Double = 1.0
)

class RatesConverter {
    companion object {
        val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun stringToRates(value: String): HashMap<String, Double> =
            gson.fromJson(value, object : TypeToken<HashMap<String, Double>>() {}.type)

        @TypeConverter
        @JvmStatic
        fun fromRatesToString(levels: HashMap<String, Double>) = Gson().toJson(levels)
    }
}