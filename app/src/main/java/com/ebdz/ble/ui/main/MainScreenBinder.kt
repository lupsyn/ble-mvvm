package com.ebdz.ble.ui.main

import com.ebdz.ble.R
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
        var text = R.string.unableToStartScanning
        when (reason) {
            BleScanException.BLUETOOTH_NOT_AVAILABLE ->
                text = R.string.errorBluetoothNotAvailable

            BleScanException.BLUETOOTH_DISABLED -> {
                text = R.string.errorBluetoothDisabled
                view.startEnableBtIntent()
            }
            BleScanException.LOCATION_PERMISSION_MISSING -> {
                text = R.string.errorLocationPermissionMissing

                view.askLocationPermission()
            }
            BleScanException.LOCATION_SERVICES_DISABLED -> {
                text = R.string.errorLocationDisabled
                view.enableLocationServices()
            }
            BleScanException.SCAN_FAILED_ALREADY_STARTED -> text = R.string.errorScanFailedAlreadyStarted
            BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> text = R.string.errorScanFailedApplicationRegistration
            BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED -> text = R.string.errorFeatureUnsupported
            BleScanException.SCAN_FAILED_INTERNAL_ERROR -> text = R.string.errorInternal
            BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> text = R.string.errorOutOfHardwareResources
            BleScanException.UNKNOWN_ERROR_CODE, BleScanException.BLUETOOTH_CANNOT_START -> text = R.string.errorUnknowError
        }
        view.displayErrorStringRes(text)
    }

    fun bindStatus(status: Boolean) {
        view.showProgressBar(status)
        view.displayErrorStringRes(if (status) R.string.scanning else R.string.stopped)
        view.toggle(status)
    }

    fun bindBleConnection(resource: Resource<String>) {

        when (resource) {
            is Resource.StartingResource -> {
                view.setFabClickable(false)
                view.showProgressBar(true)
                view.changeConnection(true)
                view.displayErrorStringRes(R.string.connecting)
            }
            is Resource.SuccessResource -> {
                view.setFabClickable(true)
                view.showProgressBar(false)
                view.displayError(resource.data!!)
            }

            is Resource.Stopping -> {
                view.setFabClickable(true)
                view.showProgressBar(false)
                view.displayToastStringRes(R.string.disconnecting)
            }
            is Resource.Stopped -> {
                view.setFabClickable(true)
                view.changeConnection(false)
                view.showProgressBar(false)
                view.displayErrorStringRes(R.string.disconnected)
            }
            is Resource.ErrorResourceString -> {
                view.setFabClickable(true)
                view.changeConnection(false)
                view.showProgressBar(false)
                view.displayToast(resource.errorMessage)
            }
            is Resource.SuccessResourceString -> {
                view.setFabClickable(true)
                view.showProgressBar(false)
                view.displayToast(resource.message)
            }
        }
    }

}