package com.example.ayosapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.BookingFillerActivity
import com.example.ayosapp.R
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemBookingsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class BookingsAdapter(
    private val context: Context,
    private val dataArrayList: ArrayList<BookingsData>
):
    RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {
    inner class BookingViewHolder(val binding: ItemBookingsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            ItemBookingsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        if(dataArrayList.isNotEmpty()) {
            val currentItem = dataArrayList[position]

            holder.binding.minibooking.setOnClickListener{
                val intent = Intent(context, BookingFillerActivity::class.java)
                intent.putExtra("UID", currentItem.UID)
                intent.putExtra("_token", currentItem._token)
                intent.putExtra("workerAssigned", currentItem.workerAssigned)
                intent.putExtra("addressID", currentItem.addressID)
                intent.putExtra("details", currentItem.details)
                intent.putExtra("timeScheduled", currentItem.timeScheduled)
                intent.putExtra("timeBooked", currentItem.timeBooked)
                intent.putExtra("timeUpdated", currentItem.timeUpdated)
                intent.putExtra("service", currentItem.service)
                intent.putExtra("initialPrice", currentItem.initialPrice)
                intent.putExtra("serviceFee", currentItem.serviceFee)
                intent.putExtra("equipmentFee", currentItem.equipmentFee)
                intent.putExtra("totalPrice", currentItem.totalPrice)
                intent.putExtra("paymentStatus", currentItem.paymentStatus)
                intent.putExtra("paymentOptions", currentItem.paymentOptions)
                intent.putExtra("status", currentItem.status)
                intent.putExtra("bookingId", currentItem.bookingId)
                intent.putExtra("fragmentTag", "bookingsDetailedFragment")
                context.startActivity(intent)
            }

            holder.binding.apply {

                statusBar.text = currentItem.service
                val time = timestampToString(currentItem.timeScheduled)
                itemDate.text = time

                itemCategory.text = currentItem.service
                when(currentItem.service){
                    "Appliance"->itemImage.setImageResource(R.drawable.home_appliance)
                    "Electrical"->itemImage.setImageResource(R.drawable.home_electrical)
                    "Aircon"->itemImage.setImageResource(R.drawable.home_aircon)
                    "Plumbing"->itemImage.setImageResource(R.drawable.home_plumbing)
                }

                if(currentItem.workerAssigned.isNullOrBlank()) {
                    itemWorker.setText(R.string.matchingworker)
                } else {
                    firestore.collection("worker").document(currentItem.workerAssigned).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                //gets name of worker to pass to textview
                                val worker1 = documentSnapshot.getString("first_name")
                                val worker2 = documentSnapshot.getString("middle_name")
                                val worker3 = documentSnapshot.getString("last_name")
                                val fullName = "$worker1 $worker2 $worker3"
                                itemWorker.text = fullName
                            } else {
                                // Document does not exist
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors
                        }
                    itemWorker.text = currentItem.workerAssigned
                }

                val totalPrice = currentItem.totalPrice?.plus(currentItem.serviceFee!!)
                    ?.plus(currentItem.initialPrice!!)?.plus(currentItem.equipmentFee!!)
                itemTotal.text = totalPrice.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
        val date = timestamp?.toDate()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date!!)
        return dateString
    }
}
