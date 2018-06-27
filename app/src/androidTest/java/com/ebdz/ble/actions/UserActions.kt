package com.ebdz.ble.actions

import android.app.Activity
import android.support.test.rule.ActivityTestRule

class UserActions(private val activityTestRule: ActivityTestRule<out Activity>) {

    fun launchesTheApp() {
        activityTestRule.launchActivity(null)
        /*
         Easiest way is to wrap it as an Rx operation and all done.
        For now, just use a good old sleep :(
         */
        try {
            Thread.sleep(1000)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}