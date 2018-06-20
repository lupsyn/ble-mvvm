package com.ebdz.ble.di.component

import com.ebdz.ble.BleApplication
import com.ebdz.ble.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    BindingsModule::class,
    ViewModelModule::class,
    RxModule::class,
    BleModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: BleApplication): Builder

        fun build(): AppComponent
    }

    fun inject(app: BleApplication)
}