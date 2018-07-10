package com.ebdz.ble.ui.main

import android.support.annotation.StringRes
import com.ebdz.ble.data.ble.model.BleDeviceContainer

interface MainView {

    fun toggle(visible: Boolean)
    fun updateResults(results: List<BleDeviceContainer>)
    fun startEnableBtIntent()
    fun askLocationPermission()
    fun enableLocationServices()
    fun changeConnection(connection: Boolean)
    fun showProgressBar(visibility: Boolean)
    fun displayToastStringRes(@StringRes errorMessage: Int)
    fun setFabClickable(value: Boolean)
    fun displayToast(errorMessage: String)
    fun displayError(data: String)
    fun displayErrorStringRes(@StringRes errorMessage: Int)
}
