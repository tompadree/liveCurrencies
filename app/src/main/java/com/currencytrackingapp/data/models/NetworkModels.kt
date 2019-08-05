package com.currencytrackingapp.data.models

import com.google.gson.annotations.SerializedName

class NetworkError {
    @SerializedName("object")
    var obj: String? = null
    @SerializedName("field")
    var field: String? = null
    @SerializedName("rejectedValue")
    var rejectedValue: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("messages")
    var messages: List<String>? = null
    @SerializedName("type")
    var type: String? = null
}

class NetworkErrors {
    @SerializedName("errorCode")
    var errorCode: Int = 0
    @SerializedName("status")
    var status: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("messages")
    var messages: List<String>? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("timestamp")
    var timestamp: Float? = null
    @SerializedName("errors")
    var errors: List<NetworkError>? = null
}