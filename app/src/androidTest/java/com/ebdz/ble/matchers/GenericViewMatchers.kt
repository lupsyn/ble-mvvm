package com.ebdz.ble.matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

object GenericViewMatchers {

    /**
     * Match the _first_ instance of the given [matcher].
     */
    fun firstInstanceOf(matcher: Matcher<View>): Matcher<View> {
        return object : TypeSafeDiagnosingMatcher<View>() {
            private var matchedItem: Any? = null

            override fun matchesSafely(item: View, mismatchDescription: Description): Boolean {
                if (matchedItem != null) {
                    return matchedItem === item
                }
                val matched = matcher.matches(item)
                if (matched) {
                    matchedItem = item
                }
                return matched
            }

            override fun describeTo(description: Description) {
                description.appendText("first instance of ")
                matcher.describeTo(description)
            }
        }
    }
}