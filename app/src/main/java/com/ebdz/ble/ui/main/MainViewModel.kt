package com.ebdz.ble.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ebdz.ble.data.Resource
import com.ebdz.ble.data.ble.BleManagerContract
import com.ebdz.ble.data.ble.model.BleDeviceContainer
import com.ebdz.ble.di.component.NamedParams
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
        private val bleManager: BleManagerContract,
        @Named(NamedParams.RX_OBSERVE_THREAD) private val observeThread: Scheduler)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var mReason: MutableLiveData<Int> = MutableLiveData()
    var mScanning: MutableLiveData<Boolean> = MutableLiveData()
    var bleDeviceContainerList: MutableLiveData<MutableList<BleDeviceContainer>> = MutableLiveData()
    var mBleConnected: MutableLiveData<Resource<String>> = MutableLiveData()
    lateinit var connectionDisposable: Disposable

    init {
        bleDeviceContainerList.value = ArrayList()

        compositeDisposable.add(bleManager.getResource()
                .observeOn(observeThread)
                .subscribe(this::updateSearchedUser))
    }

    fun toggleScan() {
        if (bleManager.isScanning()) {
            bleManager.stopScan()
        } else {
            bleManager.startScan()
        }
    }

    private fun updateSearchedUser(resource: Resource<ScanResult>) {
        when (resource) {
            is Resource.SuccessResource -> {
                val bleDevice = BleDeviceContainer.Companion.getInstance(resource.data!!)
                val list = bleDeviceContainerList.value
                val indexOf = list!!.indexOf(bleDevice)
                if (indexOf == -1) {
                    list.add(bleDevice)
                } else {
                    list[indexOf] = bleDevice
                }
                bleDeviceContainerList.value = list
            }
            is Resource.Stopped -> {
                mScanning.value = false
            }
            is Resource.ErrorResource -> {
                mReason.value = resource.errorMessage
            }
            is Resource.StartingResource -> {
                mScanning.value = true
            }
        }
    }

    fun createConnection(bleDevice: RxBleDevice) {
        //If it's connected disconnect
        if (bleDevice.connectionState == RxBleConnection.RxBleConnectionState.CONNECTED) {
            connectionDisposable.dispose()
        } else {
            bleManager.stopScan()
            connectionDisposable = bleDevice.establishConnection(false)
                    .observeOn(observeThread)
                    .doFinally({
                        connectionDisposable.dispose()
                    })
                    .subscribe({ connection -> onConnectionReceived(connection) }, { t -> onConnectionReceivedThrowable(t) })

            compositeDisposable.add(bleDevice.observeConnectionStateChanges()
                    .observeOn(observeThread)
                    .subscribe { connection -> onConnectionChanged(connection) })

            compositeDisposable.add(connectionDisposable)
        }
    }

    fun onConnectionChanged(newState: RxBleConnection.RxBleConnectionState) {
        when (newState) {
            RxBleConnection.RxBleConnectionState.CONNECTING -> {
                mBleConnected.value = Resource.StartingResource()
            }
            RxBleConnection.RxBleConnectionState.CONNECTED -> {
                mBleConnected.value = Resource.SuccessResource(newState.toString())
            }
            RxBleConnection.RxBleConnectionState.DISCONNECTING -> {
                mBleConnected.value = Resource.Stopping()
            }
            RxBleConnection.RxBleConnectionState.DISCONNECTED -> {
                mBleConnected.value = Resource.Stopped()
            }
        }
    }

    fun onConnectionReceived(connection: RxBleConnection) {
        //Connection estabilished.
        mBleConnected.value = Resource.SuccessResource("Connection received")
    }

    fun onConnectionReceivedThrowable(throwable: Throwable) {
        mBleConnected.value = Resource.ErrorResourceString("Connection error: " + throwable.message)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        bleManager.stopScan()
    }
}
