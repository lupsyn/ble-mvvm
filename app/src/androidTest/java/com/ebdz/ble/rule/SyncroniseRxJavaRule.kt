package com.ebdz.ble.rule

import android.os.AsyncTask
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.rules.ExternalResource

/**
 * This JUnit rule synchronises RxJava2 with Espresso.
 * It swaps the default RxJava [Schedulers] with one backed by [AsyncTask]'s thread pool,
 * which is already in sync with Espresso.
 *
 * This approach has the limitation of a limited number of threads available for RxJava,
 * equal to size of [AsyncTask.THREAD_POOL_EXECUTOR]. If you have a larger number of
 * parallel operations you might exhaust the available threads,
 * in which case the behaviour of the test becomes different than the real app.
 *
 * Usage: Just add this as a JUnit [Rule] in your test class and you're synchronized with RxJava2.
 */
class SyncroniseRxJavaRule : ExternalResource() {

    private val asyncTaskScheduler = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)

    override fun before() {
        RxJavaPlugins.setIoSchedulerHandler { asyncTaskScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { asyncTaskScheduler }
    }

    override fun after() {
        RxJavaPlugins.reset()
    }
}