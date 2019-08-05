package com.currencytrackingapp.data.models

sealed class Response<T>

data class ResponseSucces<T>(val data: T) : Response<T>()

data class ResponseError<T>(val t: Throwable) : Response<T>()