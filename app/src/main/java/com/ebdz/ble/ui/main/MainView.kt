package com.ebdz.ble.ui.main

import com.ebdz.ble.data.ble.model.BleDeviceContainer

interface MainView {
    fun displayError(errorMessage: String)
    fun toggle(visible: Boolean)
    fun updateResults(results: List<BleDeviceContainer>)
    fun startEnableBtIntent()
    fun askLocationPermission()
    fun enableLocationServices()
    fun changeConnection(connection: Boolean)
    fun showProgressBar(visibility: Boolean)
    fun displayToastError(errorMessage: String)
    fun setFabClickable(value: Boolean)
}
