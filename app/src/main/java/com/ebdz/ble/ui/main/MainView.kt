package com.ebdz.ble.ui.main

import com.ebdz.ble.data.ble.model.BleDevice

interface MainView {
    fun displayError(errorMessage: String)
    fun toggle(visible: Boolean)
    fun updateResults(results: List<BleDevice>)
    fun startEnableBtIntent()
    fun askLocationPermission()
    fun enableLocationServices()
}
