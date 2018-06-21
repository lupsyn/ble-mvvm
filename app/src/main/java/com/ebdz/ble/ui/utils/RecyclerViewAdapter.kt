package com.ebdz.ble.ui.utils

import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ebdz.ble.R
import com.ebdz.ble.data.ble.model.BleDeviceContainer
import com.ebdz.ble.ui.main.BleDeviceDiffCallback
import com.polidea.rxandroidble2.RxBleDevice
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class RecyclerViewAdapter(private val mDeviceContainerList: MutableList<BleDeviceContainer>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    private val clickSubject = PublishSubject.create<Pair<RxBleDevice, Int>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        val recyclerViewHolder = RecyclerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_ble_device, parent, false))
        recyclerViewHolder.itemView.setOnClickListener {
            clickSubject.onNext(Pair(mDeviceContainerList[recyclerViewHolder.adapterPosition].bleDevice, recyclerViewHolder.adapterPosition))
        }
        return recyclerViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val borrowModel = mDeviceContainerList[position]
        holder.name.text = if (TextUtils.isEmpty(borrowModel.name)) "n/a" else borrowModel.name
        holder.mac.text = borrowModel.macAddress
        holder.rssi.text = borrowModel.rssi.toString()
    }

    override fun getItemCount(): Int {
        return mDeviceContainerList.size
    }

    fun addItems(newList: List<BleDeviceContainer>) {
        val diffResult = DiffUtil.calculateDiff(BleDeviceDiffCallback(mDeviceContainerList, newList))
        mDeviceContainerList.clear()
        mDeviceContainerList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun onItemClick(): Flowable<Pair<RxBleDevice, Int>> {
        return clickSubject.toFlowable(BackpressureStrategy.DROP)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.card_view)
        val name: TextView = view.findViewById(R.id.name)
        val mac: TextView = view.findViewById(R.id.mac)
        val rssi: TextView = view.findViewById(R.id.rssi)
    }
}