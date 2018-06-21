package com.ebdz.ble.ui.main

import android.support.v7.util.DiffUtil
import com.ebdz.ble.data.ble.model.BleDeviceContainer

class BleDeviceDiffCallback(private val oldDeviceContainers: List<BleDeviceContainer>?,
                            private val newDeviceContainers: List<BleDeviceContainer>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldDeviceContainers?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newDeviceContainers?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDeviceContainers!![oldItemPosition].macAddress == newDeviceContainers!![newItemPosition].macAddress
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newProduct = newDeviceContainers!![newItemPosition]
        val oldProduct = oldDeviceContainers!![oldItemPosition]
        return (newProduct.macAddress == oldProduct.macAddress
                && newProduct.name == oldProduct.name
                && newProduct.rssi == oldProduct.rssi)
    }

}