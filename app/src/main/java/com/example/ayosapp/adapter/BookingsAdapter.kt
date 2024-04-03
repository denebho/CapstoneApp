package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemBookingsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BookingsAdapter(
    private val context: Context,
    private val dataArrayList: ArrayList<BookingsData>
):
    RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {
    inner class BookingViewHolder(val binding: ItemBookingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            ItemBookingsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        if(dataArrayList.isNotEmpty()) {
            val booking = dataArrayList[position]

            holder.binding.apply{
                val time = timestampToString(booking.timeScheduled)
                val total = booking.totalPrice?.plus(booking.serviceFee!!)
                    ?.plus(booking.initialPrice!!)?.plus(booking.additionalFee!!)
                when(booking.service){
                    "Appliance"->itemImage.setImageResource(R.drawable.home_appliance)
                    "Electrical"->itemImage.setImageResource(R.drawable.home_electrical)
                    "Aircon"->itemImage.setImageResource(R.drawable.home_aircon)
                    "Plumbing"->itemImage.setImageResource(R.drawable.home_plumbing)
                }
                statusBar.text = booking.status
                itemDate.text = time
                itemCategory.text = booking.service
                itemTotal.text = total.toString()
                if(booking.workerAssigned.isNullOrBlank()) {
                    itemWorker.setText(R.string.matchingworker)
                }else {
                    itemWorker.text = booking.workerAssigned
                }
            }
        }
    }
    fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
        val date = timestamp?.toDate()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date)
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timeString = timeFormat.format(date)

        return "$dateString at $timeString"
    }
}