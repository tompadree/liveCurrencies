package com.currencytrackingapp.data.models

import com.google.gson.annotations.SerializedName

class NetworkError {
    @SerializedName("object")
    var obj: String? = null
    @SerializedName("field")
    var field: String? = null
    @SerializedName("rejectedValue")
    var rejectedValue: String? = null
    @SerializedName("error")
    var message: String? = null
    @SerializedName("type")
    var type: String? = null
}