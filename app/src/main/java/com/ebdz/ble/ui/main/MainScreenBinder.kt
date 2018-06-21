package com.ebdz.ble.ui.main

import com.ebdz.ble.data.Resource
import com.ebdz.ble.data.ble.model.BleDeviceContainer
import com.polidea.rxandroidble2.exceptions.BleScanException
import javax.inject.Inject

/**
 * Binds complex UI objects to Android's View system.
 *
 * It's a lightweight presenter from the MVP world.
 */

class MainScreenBinder @Inject constructor(private val view: MainView) {

    fun bindResult(results: List<BleDeviceContainer>) {
        view.showProgressBar(false)
        view.updateResults(results)
    }

    fun bindError(reason: Int) {
        view.showProgressBar(false)
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
        view.showProgressBar(status)
        view.displayError(if (status) "Scanning" else "Stopped")
        view.toggle(status)
    }

    fun bindBleConnection(resource: Resource<String>) {

        when (resource) {
            is Resource.StartingResource -> {
                view.setFabClickable(false)
                view.showProgressBar(true)
                view.changeConnection(true)
                view.displayError("Connecting...")
            }
            is Resource.SuccessResource -> {
                view.setFabClickable(true)
                view.showProgressBar(false)
                view.displayError("Connected to " + resource.data!!)
            }

            is Resource.Stopping -> {
                view.setFabClickable(true)
                view.showProgressBar(false)
                view.displayError("Disconnecting...")
            }
            is Resource.Stopped -> {
                view.setFabClickable(true)
                view.changeConnection(false)
                view.showProgressBar(false)
                view.displayError("Disconnected")
            }
            is Resource.ErrorResourceString -> {
                view.setFabClickable(true)
                view.changeConnection(false)
                view.showProgressBar(false)
                view.displayToastError(resource.errorMessage)
            }
        }
    }

}