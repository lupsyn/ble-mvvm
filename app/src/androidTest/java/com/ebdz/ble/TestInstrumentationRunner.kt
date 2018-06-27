package com.ebdz.ble

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

@Suppress("unused") // set as a testInstrumentationRunner by full class name
class TestInstrumentationRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BleApplication::class.java.name, context)
    }
}