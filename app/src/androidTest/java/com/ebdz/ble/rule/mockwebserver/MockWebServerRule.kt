package com.ebdz.ble.rule.mockwebserver

import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matcher
import org.junit.rules.ExternalResource
import java.io.IOException

/**
 * Used to initiate and program an HTTP server running locally on the device.
 * All API requests from the app need to be directed to this server,
 * so we maintain full control over the responses served.
 *
 * The server is lightweight enough to be started / stopped for each test.
 */
class MockWebServerRule: ExternalResource() {

    private val server = MockWebServer()
    private val dispatcher = MockDispatcher()

    override fun before() {
        server.setDispatcher(dispatcher)
        server.start(SERVER_PORT)
    }

    override fun after() {
        shutdownServer()
    }

    private fun shutdownServer() {
        try {
            dispatcher.shutdown()
            server.shutdown()
        } catch (e: IOException) {
            throw RuntimeException("Failed to shutdown MockWebServer", e)
        }
    }

    /**
     * Add a new response to the mock server.
     */
    fun respondsWith(mockResponseBuilder: MockResponseBuilder) = ResponseSetup(mockResponseBuilder)

    /**
     * Just adds sugar to the syntax for setting up a server response.
     * It allows for syntax like:
     *
     * _serverRule.respondsWith(awesomeResponse).on(requestMatcher)_
     */
    inner class ResponseSetup(private val responseBuilder: MockResponseBuilder) {
        fun on(requestMatcher: Matcher<in RecordedRequest>) {
            dispatcher.responds(requestMatcher, responseBuilder)
        }
    }

    companion object {
        const val SERVER_PORT = 9999
    }
}