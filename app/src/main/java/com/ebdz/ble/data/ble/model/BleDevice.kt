package com.ebdz.ble.data.ble.model

import android.os.Parcelable
import com.polidea.rxandroidble2.scan.ScanResult
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BleDevice(
        val macAddress: String,
        val name: String?,
        val rssi: Int) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other != null
                && other is BleDevice
                && other.macAddress.equals(macAddress, ignoreCase = true))
    }

    override fun hashCode(): Int {
        var result = macAddress.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + rssi
        return result
    }

    companion object {
        fun getInstance(scanResult: ScanResult): BleDevice {
            val bleDevice = scanResult.bleDevice
            return BleDevice(bleDevice.macAddress, bleDevice.name, scanResult.rssi)
        }
    }
}