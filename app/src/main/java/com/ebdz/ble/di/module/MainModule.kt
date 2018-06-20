package com.ebdz.ble.di.module

import com.ebdz.ble.ui.main.MainView
import com.ebdz.ble.ui.main.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainModule {

    @Binds
    abstract fun provideMainView(searchUserActivity: MainActivity): MainView
}