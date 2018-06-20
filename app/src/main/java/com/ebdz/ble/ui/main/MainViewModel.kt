package com.ebdz.ble.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ebdz.ble.data.Resource
import com.ebdz.ble.data.ble.BleManagerContract
import com.ebdz.ble.data.ble.model.BleDevice
import com.ebdz.ble.di.component.NamedParams
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
        private val bleManager: BleManagerContract,
        @Named(NamedParams.RX_OBSERVE_THREAD) private val observeThread: Scheduler)
    : ViewModel() {

    private val subscriptions = CompositeDisposable()

    var mReason: MutableLiveData<Int> = MutableLiveData()
    var mScanning: MutableLiveData<Boolean> = MutableLiveData()
    var bleDeviceList: MutableLiveData<MutableList<BleDevice>> = MutableLiveData()

    @Inject
    lateinit var binder: MainScreenBinder


    init {
        bleDeviceList.value = ArrayList()

        subscriptions.add(bleManager.getResource()
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
                val bleDevice = BleDevice.Companion.getInstance(resource.data!!)
                val list = bleDeviceList.value
                val indexOf = list!!.indexOf(bleDevice)
                if (indexOf == -1) {
                    list.add(bleDevice)
                } else {
                    list[indexOf] = bleDevice
                }
                bleDeviceList.value = list
            }
            is Resource.StoppingResource -> {
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

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
        bleManager.stopScan()
    }
}
