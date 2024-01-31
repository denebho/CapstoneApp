package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ayosapp.R
import com.example.ayosapp.data.AyosData

class AyosAdapter(context: Context, dataArrayList: ArrayList<AyosData>):
    ArrayAdapter<AyosData>(context, R.layout.fragment_ayosnow, dataArrayList) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val serviceData = getItem(position)

        var convertView = view

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_services, parent, false)
        }
        val serviceIcon = convertView!!.findViewById<ImageView>(R.id.serviceIcon)
        val serviceType = convertView.findViewById<TextView>(R.id.serviceType)
        val serviceProvider = convertView.findViewById<TextView>(R.id.serviceProvider)
        val serviceDescription = convertView.findViewById<TextView>(R.id.serviceDescription)

        serviceIcon.setImageResource(serviceData!!.image)
        serviceType.text = serviceData.serviceType
        serviceProvider.text = serviceData.serviceProvider
        serviceDescription.text = serviceData.serviceDescription

        /*bookingImage.setImageResource(bookingsData!!.image)
        bookingStatus.text = bookingsData.status.toString()
        bookingDate.text = bookingsData.dateofService
        bookingCategory.text = bookingsData.serviceOffered.toString()
        bookingWorker.text = bookingsData.workerName
        bookingTotal.text = bookingsData.totalPrice.toString()*/

        return convertView

    }
}