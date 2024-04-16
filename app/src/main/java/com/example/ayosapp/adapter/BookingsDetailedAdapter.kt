package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemBookingsDetailedBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class BookingsDetailedAdapter(
    private val context: Context,
    private val dataArrayList: ArrayList<BookingsData>,
    private var reportClickListener: ReportClickListener? = null

): RecyclerView.Adapter<BookingsDetailedAdapter.BookingViewHolder>() {
    inner class BookingViewHolder(val binding: ItemBookingsDetailedBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            ItemBookingsDetailedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        if(dataArrayList.isNotEmpty()) {
            val currentItem = dataArrayList[position]

            holder.binding.reportBtn.setOnClickListener {
                reportClickListener?.onReportClick(currentItem)
            }

            holder.binding.apply {

                when(currentItem.service){
                    "Appliance"->itemImage.setImageResource(R.drawable.home_appliance)
                    "Electrical"->itemImage.setImageResource(R.drawable.home_electrical)
                    "Aircon"->itemImage.setImageResource(R.drawable.home_aircon)
                    "Plumbing"->itemImage.setImageResource(R.drawable.home_plumbing)
                }
                statusBar.text = currentItem.service
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

                firestore.collection("address").document(currentItem.addressID!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            //gets address line of address to pass to textview
                            val addressline = documentSnapshot.getString("address")
                            itemAddress.text = addressline
                        } else {
                            // Document does not exist
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
                val time = timestampToString(currentItem.timeScheduled)
                itemDate.text = time
                itemCategory.text = currentItem.service

                itemInitPrice.text = currentItem.initialPrice.toString()
                itemServiceFee.text = currentItem.serviceFee.toString()
                itemEquipmentFee.text = currentItem.equipmentFee.toString()

                val totalPrice = currentItem.totalPrice?.plus(currentItem.serviceFee!!)
                    ?.plus(currentItem.initialPrice!!)?.plus(currentItem.equipmentFee!!)
                itemTotal.text = totalPrice.toString()

                itemPaymentStatus.text = currentItem.paymentStatus
                itemPaymentOptions.text = currentItem.paymentOptions

                bookingId.text = currentItem.bookingId

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

    interface ReportClickListener {
        fun onReportClick(bookingData: BookingsData)
    }

}