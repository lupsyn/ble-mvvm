package com.ebdz.ble.data.ble

import com.ebdz.ble.data.Resource
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.Flowable

interface BleManagerContract {

    fun getResource(): Flowable<Resource<ScanResult>>
    fun startScan()
    fun stopScan()
    fun isScanning(): Boolean

}
