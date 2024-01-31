package com.example.ayosapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class BookingsAdapter(context: Context, dataArrayList: ArrayList<BookingsData>) :
ArrayAdapter<BookingsData>(context, R.layout.item_bookings, dataArrayList) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val bookingsData = getItem(position)

        var convertView = view

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookings, parent, false)
        }

        val bookingStatus = convertView!!.findViewById<TextView>(R.id.statusBar)
        val bookingDate = convertView.findViewById<TextView>(R.id.itemDate)
        val bookingImage = convertView.findViewById<ImageView>(R.id.itemImage)
        val bookingCategory = convertView.findViewById<TextView>(R.id.itemCategory)
        val bookingWorker = convertView.findViewById<TextView>(R.id.itemWorker)
        val bookingTotal = convertView.findViewById<TextView>(R.id.itemTotal)

        bookingImage.setImageResource(bookingsData!!.image)
        bookingStatus.text = bookingsData.status
        bookingDate.text = bookingsData.dateofService
        bookingCategory.text = bookingsData.serviceOffered
        bookingWorker.text = bookingsData.workerName
        bookingTotal.text = bookingsData.totalPrice.toString()

        return convertView

    }

}