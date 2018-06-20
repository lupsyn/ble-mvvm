package com.ebdz.ble

import android.app.Activity
import android.app.Application
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import com.ebdz.ble.di.component.AppComponent
import com.ebdz.ble.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class BleApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        initAppComponent().inject(this)
    }

    @VisibleForTesting
    protected open fun initAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .app(this)
                .build()
    }

    override fun activityInjector() = activityInjector

    override fun supportFragmentInjector() = fragmentInjector
}