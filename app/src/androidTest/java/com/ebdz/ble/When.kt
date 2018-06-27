package com.ebdz.ble

import android.app.Activity
import android.support.test.rule.ActivityTestRule
import com.ebdz.ble.actions.UserActions


class When(activityRule: ActivityTestRule<out Activity>) {

    val user = UserActions(activityRule)
}
