package com.ebdz.ble.di.module

import com.ebdz.ble.BleApplication
import com.polidea.rxandroidble2.RxBleClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BleModule {

    @Singleton
    @Provides
    fun provideRxBleClient(application: BleApplication): RxBleClient {
        return RxBleClient.create(application)
    }
}