package com.ebdz.ble.ui.main

import android.Manifest
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.ebdz.ble.R
import com.ebdz.ble.data.ble.model.BleDevice
import com.ebdz.ble.ui.utils.RecyclerViewAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {
    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var binder: MainScreenBinder

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var mainViewModel: MainViewModel? = null
    private var rxPermissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initRxPermissions()
        initViews()
        initViewModel()
    }

    private fun initRxPermissions() {
        rxPermissions = RxPermissions(this)
        rxPermissions!!.setLogging(true)
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            mainViewModel?.toggleScan()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(ArrayList())
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel!!.mReason.observe(this, Observer<Int> { reason -> binder.bindError(reason!!) })
        mainViewModel!!.mScanning.observe(this, Observer<Boolean> { status -> binder.bindStatus(status!!) })
        mainViewModel!!.bleDeviceList.observe(this, Observer<MutableList<BleDevice>> { binder.bindResult(it!!) })
    }

    private fun onPermissionChanged(granted: Boolean?) {
        Log.i(PERMISSION_TAG, "Permissions" + (if (granted!!) "" else " not") + " Granted")
        if (granted) {
            // All requested permissions are granted
            mainViewModel?.toggleScan()
        } else {
            // At least one permission is denied
            displayError("Need permissions to run")
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    override fun updateResults(results: List<BleDevice>) {
        (recyclerView.adapter as RecyclerViewAdapter).addItems(results)
    }

    override fun enableLocationServices() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    override fun askLocationPermission() {
        rxPermissions!!.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe({ this.onPermissionChanged(it) })
    }

    override fun startEnableBtIntent() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivity(enableBtIntent)
    }

    override fun toggle(visible: Boolean) {
        fab.setImageResource(if (visible) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play)
    }

    override fun displayError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    companion object {
        private val PERMISSION_TAG = "RxPermissions"
        private val TAG = MainActivity::class.java.simpleName
    }

}