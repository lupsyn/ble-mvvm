package com.ebdz.ble.data.ble.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanResult
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
//Bug into parcelize
@SuppressLint("ParcelCreator")
@Parcelize
data class BleDeviceContainer(
        var bleDevice: @RawValue RxBleDevice,
        val macAddress: String,
        val name: String?,
        val rssi: Int) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other != null
                && other is BleDeviceContainer
                && other.macAddress.equals(macAddress, ignoreCase = true))
    }

    override fun hashCode(): Int {
        var result = macAddress.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + rssi
        return result
    }

    companion object {
        fun getInstance(scanResult: ScanResult): BleDeviceContainer {
            val bleDevice = scanResult.bleDevice
            return BleDeviceContainer(bleDevice, bleDevice.macAddress, bleDevice.name, scanResult.rssi)
        }
    }

}
