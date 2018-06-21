package com.ebdz.ble.data.ble


import com.ebdz.ble.data.Resource
import com.ebdz.ble.di.component.NamedParams
import com.jakewharton.rxrelay2.PublishRelay
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

class BleManager @Inject constructor(
        private val rxBleClient: RxBleClient,
        @Named(NamedParams.RX_WORKER_THREAD) private val workerThread: Scheduler
) : BleManagerContract {

    lateinit var disposeScan: Disposable
    var isScan: Boolean = false
    private var rxRelay: PublishRelay<Resource<ScanResult>> = PublishRelay.create()

    override fun startScan() {
        isScan = true
        disposeScan = (rxBleClient.scanBleDevices(ScanSettings.Builder().build())
                .subscribeOn(workerThread)
                .doOnDispose({ onDoOnDispose() })
                .doOnSubscribe({ onDoOnSubscribe() })
                .subscribe(this::onScanResult, this::onScanFailure))
    }

    override fun stopScan() {
        isScan = false
        disposeScan.dispose()
    }

    override fun getResource(): Flowable<Resource<ScanResult>> {
        return rxRelay.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun isScanning(): Boolean {
        return isScan
    }

    private fun onDoOnDispose() {
        rxRelay.accept(Resource.Stopped())
    }

    fun onDoOnSubscribe() {
        rxRelay.accept(Resource.StartingResource())
    }

    fun onScanResult(result: ScanResult) {
        rxRelay.accept(Resource.SuccessResource(result))
    }

    fun onScanFailure(reason: Throwable) {
        rxRelay.accept(Resource.ErrorResource((reason as BleScanException).getReason()))
    }
}

