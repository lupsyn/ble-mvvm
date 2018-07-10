package com.ebdz.ble.ui.main

import android.Manifest
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.ebdz.ble.R
import com.ebdz.ble.data.Resource
import com.ebdz.ble.data.ble.model.BleDeviceContainer
import com.ebdz.ble.ui.utils.RecyclerViewAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.view_ble_device.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {
    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var binder: MainScreenBinder

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var mainViewModel: MainViewModel? = null
    private var rxPermissions: RxPermissions? = null
    private var positionItemClicked: Int = -1
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initRxPermissions()
        initViews()
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        // Item clicked and ble found should be saved and restored into the bundle if the activity is destroyed
        // outState?.putInt("POSITION_ITEM_CLICKED", positionItemClicked)
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
        adapter = RecyclerViewAdapter(ArrayList())
        recyclerView.adapter = adapter

        (recyclerView.adapter as RecyclerViewAdapter).onItemClick().subscribe {
            mainViewModel?.createConnection(it.first)
            positionItemClicked = it.second
        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel!!.mReason.observe(this, Observer<Int> { reason -> binder.bindError(reason!!) })
        mainViewModel!!.mScanning.observe(this, Observer<Boolean> { status -> binder.bindStatus(status!!) })
        mainViewModel!!.bleDeviceContainerList.observe(this, Observer<MutableList<BleDeviceContainer>> { binder.bindResult(it!!) })
        mainViewModel!!.mBleConnected.observe(this, Observer<Resource<String>> { binder.bindBleConnection(it!!) })
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

    override fun updateResults(results: List<BleDeviceContainer>) {
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


    override fun changeConnection(connection: Boolean) {
        recyclerView.findViewHolderForAdapterPosition(positionItemClicked).itemView.card_view.setCardBackgroundColor(if (connection) Color.RED else Color.TRANSPARENT)
    }

    override fun showProgressBar(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun setFabClickable(value: Boolean) {
        fab.isClickable = value
    }

    override fun displayError(errorMessage: String) {
        Snacky.builder().setActivity(this).setText(errorMessage).build().show()
    }

    override fun displayErrorStringRes(errorMessage: Int) {
        Snacky.builder().setActivity(this).setText(resources.getString(errorMessage)).build().show()
    }

    override fun displayToastStringRes(errorMessage: Int) {
        Toast.makeText(this, resources.getString(errorMessage), Toast.LENGTH_LONG).show()
    }

    override fun displayToast(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    companion object {
        private val PERMISSION_TAG = "RxPermissions"
    }
}