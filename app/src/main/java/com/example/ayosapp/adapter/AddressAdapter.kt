package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ayosapp.R
import com.example.ayosapp.data.AddressData


class AddressAdapter(context: Context, dataArrayList: ArrayList<AddressData>):
    ArrayAdapter<AddressData>(context, R.layout.fragment_address, dataArrayList) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val addressData = getItem(position)

        var convertView = view

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false)
        }
        val serviceIcon = convertView!!.findViewById<ImageView>(R.id.serviceIcon)
        val address1 = convertView.findViewById<TextView>(R.id.itemAddress1)
        val address2 = convertView.findViewById<TextView>(R.id.itemAddress2)
        val fullName = convertView.findViewById<TextView>(R.id.itemName)

        address1.text = addressData?.address1
        address2.text = addressData?.address2
        fullName.text = addressData?.fullName

        return convertView

    }
}