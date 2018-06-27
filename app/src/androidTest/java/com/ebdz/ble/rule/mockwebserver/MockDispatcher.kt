package com.ebdz.ble.rule.mockwebserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import java.net.HttpURLConnection

/**
 * Matches incoming server requests to appropriate responses.
 */
class MockDispatcher : Dispatcher() {

    private val responseMap = linkedMapOf<Matcher<in RecordedRequest>, MockResponseBuilder>()

    /**
     * Match the incoming [request] to an appropriate [MockResponse].
     *
     * Returns a [HttpURLConnection.HTTP_NOT_FOUND] error if a response for the given [request]
     * wasn't found (isn't setup in the current test).
     */
    override fun dispatch(request: RecordedRequest): MockResponse {
        val mismatchDescription = StringDescription()
        val response = matchRequest(request, mismatchDescription)
        return response?.mockResponse ?: notFoundError(request)
    }

    private fun matchRequest(request: RecordedRequest, mismatchDescription: StringDescription): MockResponseBuilder? {
        for ((requestMatcher, response) in responseMap) {
            if (requestMatcher.matches(request)) {
                return response
            } else {
                describeMismatch(mismatchDescription, requestMatcher, request)
            }
        }

        return null
    }

    private fun describeMismatch(mismatchDescription: StringDescription, requestMatcher: Matcher<in RecordedRequest>, request: RecordedRequest) {
        mismatchDescription
                .appendText("\n--> Matcher:\n")
                .appendText("\t")
                .appendDescriptionOf(requestMatcher)
                .appendText("\n")
        requestMatcher.describeMismatch(request, mismatchDescription)
    }

    private fun notFoundError(request: RecordedRequest) = MockResponse()
            .setBody("No response found for request: $request")
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)

    /**
     * Associate the given [requestMatcher] to the [responseBuilder].
     */
    fun responds(requestMatcher: Matcher<in RecordedRequest>, responseBuilder: MockResponseBuilder) {
        responseMap[requestMatcher] = responseBuilder
    }
}
