package com.ebdz.ble.rule.mockwebserver

import okhttp3.mockwebserver.MockResponse
import java.net.HttpURLConnection

/**
 * Provides fluent API to construct a [MockResponse].
 */
class MockResponseBuilder private constructor() {
    val mockResponse = MockResponse()

    fun responseCode(responseCode: Int): MockResponseBuilder {
        mockResponse.setResponseCode(responseCode)
        return this
    }

    fun body(body: String): MockResponseBuilder {
        mockResponse.setBody(body)
        return this
    }

    fun header(name: String, value: String): MockResponseBuilder {
        mockResponse.setHeader(name, value)
        return this
    }

    companion object {
        fun withResponse() = MockResponseBuilder()

        fun withSuccessfulResponse() = withResponse().responseCode(HttpURLConnection.HTTP_OK)

        fun withNotFoundErrorResponse() = withResponse().responseCode(HttpURLConnection.HTTP_NOT_FOUND)
    }
}