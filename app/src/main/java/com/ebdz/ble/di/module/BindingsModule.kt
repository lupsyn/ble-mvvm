package com.ebdz.ble.di.module

import com.ebdz.ble.data.ble.BleManager
import com.ebdz.ble.data.ble.BleManagerContract
import dagger.Binds
import dagger.Module

@Module
abstract class BindingsModule {
    @Binds
    abstract fun provideBleManager(bleManger: BleManager): BleManagerContract
}
