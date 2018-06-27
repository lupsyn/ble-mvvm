package com.ebdz.ble.arrangements

import com.ebdz.ble.matchers.RecordedRequestMatchers
import com.ebdz.ble.matchers.RecordedRequestMatchers.oneCallTo
import com.ebdz.ble.rule.mockwebserver.MockResponseBuilder
import com.ebdz.ble.rule.mockwebserver.MockWebServerRule
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matcher

/**
 * Base class for server arrangements used to mock specific set of API calls.
 *
 * It's a centralised place for dealing with the [MockWebServerRule]
 * and forces a certain structure in mocking API calls.
 */
abstract class ApiServerArrangements(private val serverRule: MockWebServerRule) {

    /**
     * Setup a server response rule that will return a **one-time** response on the given path.
     *
     * This handles all needed formatting of the given path
     * so it can be called directly with the raw path of the API call being set up,
     * e.g. "/users/123/".
     */
    fun respondOn(path: String, response: MockResponseBuilder) {
        respondOn(formattedPath(path), response)
    }

    /**
     * Setup a server response rule that will return a **one-time** response
     * for a call to that matches the given requestMatcher.
     *
     * Common use-case for this is setting multiple matchers for a given request,
     * e.g. to verify the path, HTTP method, query params, etc.
     *
     * If you just want to setup a simple response based on the request path, use [ApiServerArrangements.respondOn] instead.
     */
    fun respondOn(requestMatcher: Matcher<in RecordedRequest>, response: MockResponseBuilder) {
        serverRule.respondsWith(response).on(oneCallTo(requestMatcher))
    }

    /**
     * Extend the given {@param path} with the base API path of this class.
     *
     * @return a fully working matcher that will capture API calls made from this API on the given path.
     */
    private fun formattedPath(path: String) = RecordedRequestMatchers.pathContains(path)
}
