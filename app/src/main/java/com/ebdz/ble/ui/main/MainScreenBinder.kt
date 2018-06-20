package com.ebdz.ble.ui.main

import com.ebdz.ble.data.ble.model.BleDevice
import com.polidea.rxandroidble2.exceptions.BleScanException
import javax.inject.Inject

/**
 * Binds complex UI objects to Android's View system.
 *
 * It's a lightweight presenter from the MVP world.
 */

class MainScreenBinder @Inject constructor(private val view: MainView) {

    fun bindResult(results: List<BleDevice>) {
        view.updateResults(results)
    }

    fun bindError(reason: Int) {
        view.toggle(false)
        var text = "Unable to start scanning"
        when (reason) {
            BleScanException.BLUETOOTH_NOT_AVAILABLE ->
                text = "Bluetooth is not available"

            BleScanException.BLUETOOTH_DISABLED -> {
                text = "Enable bluetooth and try again"
                view.startEnableBtIntent()
            }
            BleScanException.LOCATION_PERMISSION_MISSING -> {
                text = "On Android 6.0 location permission is required. Implement Runtime Permissions"
                view.askLocationPermission()
            }
            BleScanException.LOCATION_SERVICES_DISABLED -> {
                text = "Location services needs to be enabled on Android 6.0"
                view.enableLocationServices()
            }
            BleScanException.SCAN_FAILED_ALREADY_STARTED -> text = "Scan with the same filters is already started"
            BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> text = "Failed to register application for bluetooth scan"
            BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED -> text = "Scan with specified parameters is not supported"
            BleScanException.SCAN_FAILED_INTERNAL_ERROR -> text = "Scan failed due to internal error"
            BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> text = "Scan cannot start due to limited hardware resources"
            BleScanException.UNKNOWN_ERROR_CODE, BleScanException.BLUETOOTH_CANNOT_START -> text = "Unable to start scanning"
        }
        view.displayError(text)
    }

    fun bindStatus(status: Boolean) {
        view.displayError(if (status) "Scanning" else "Stopped")
        view.toggle(status)
    }

}