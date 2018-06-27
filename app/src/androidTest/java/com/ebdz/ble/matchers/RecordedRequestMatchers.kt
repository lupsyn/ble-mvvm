package com.ebdz.ble.matchers

import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

object RecordedRequestMatchers {

    /**
     * Match requests that _contain_ the given path.
     */
    fun pathContains(path: String): Matcher<RecordedRequest> {
        return object : TypeSafeDiagnosingMatcher<RecordedRequest>() {
            override fun matchesSafely(recordedRequest: RecordedRequest, mismatchDescription: Description): Boolean {
                return recordedRequest.path.contains(path)
            }

            override fun describeTo(description: Description) {
                description.appendText("a request that contains the path: $path")
            }
        }
    }

    /**
     * Wrap the given [matcher] in one that will *only match once*.
     * All subsequent checks will fail.
     */
    fun oneCallTo(matcher: Matcher<in RecordedRequest>): Matcher<RecordedRequest> {
        return object : TypeSafeDiagnosingMatcher<RecordedRequest>() {
            private var matchCount = 0

            override fun matchesSafely(recordedRequest: RecordedRequest, mismatchDescription: Description): Boolean {
                if (matchCount == 1) {
                    mismatchDescription.appendText("request $recordedRequest already matched before")
                    return false
                }

                val matched = matcher.matches(recordedRequest)
                if (matched) {
                    matchCount++
                }

                return matched
            }

            override fun describeTo(description: Description) {
                description.appendText("One call to ").appendDescriptionOf(matcher)
            }
        }
    }
}