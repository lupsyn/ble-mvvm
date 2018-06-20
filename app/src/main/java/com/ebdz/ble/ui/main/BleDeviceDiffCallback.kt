package com.ebdz.ble.ui.main

import android.support.v7.util.DiffUtil
import com.ebdz.ble.data.ble.model.BleDevice

class BleDeviceDiffCallback(private val oldDevices: List<BleDevice>?, private val newDevices: List<BleDevice>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldDevices?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newDevices?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDevices!![oldItemPosition].macAddress == newDevices!![newItemPosition].macAddress
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newProduct = newDevices!![newItemPosition]
        val oldProduct = oldDevices!![oldItemPosition]
        return (newProduct.macAddress == oldProduct.macAddress
                && newProduct.name == oldProduct.name
                && newProduct.rssi == oldProduct.rssi)
    }

}