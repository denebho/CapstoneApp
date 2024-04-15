package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemBookingsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class BookingsAdapter(
    private val context: Context,
    private val bookingsList: ArrayList<BookingsData>
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
        if(bookingsList.isNotEmpty()) {
            val currentItem = bookingsList[position]

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
                    firestore.collection("booking").document(currentItem.workerAssigned).get()
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
        return bookingsList.size
    }

    fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
        val date = timestamp?.toDate()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date!!)
        return dateString
    }
}