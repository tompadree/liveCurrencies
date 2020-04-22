package com.currencytrackingapp.data.models

import com.google.gson.annotations.SerializedName

class NetworkError {
    @SerializedName("error")
    var message: String? = null
    @SerializedName("type")
    var type: String? = null
}