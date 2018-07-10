package com.ebdz.ble.di.module

import com.ebdz.ble.di.component.NamedParams
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxModule {

    @Singleton
    @Provides
    @Named(NamedParams.RX_OBSERVE_THREAD)
    fun provideRxObserveThread(): Scheduler = AndroidSchedulers.mainThread()

    @Singleton
    @Provides
    @Named(NamedParams.RX_WORKER_THREAD)
    fun provideRxWorkerThread(): Scheduler = Schedulers.io()
}
