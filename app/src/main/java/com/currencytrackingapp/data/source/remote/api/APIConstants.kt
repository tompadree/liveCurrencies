package com.currencytrackingapp.data.source.remote.api

interface APIConstants {
    companion object {

        const val SERVER_URL = "https://revolut.duckdns.org"

        const val LATEST_RATES = "/latest"

        const val CONTENT_TYPE_JSON = "Content-Type: application/json"
        const val CONTENT_TYPE_TEXT = "Content-Type: text/plain"

    }
}