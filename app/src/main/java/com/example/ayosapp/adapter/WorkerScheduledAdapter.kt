package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemScheduledWorkerBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class WorkerScheduledAdapter(
    private val context: Context,
private val bookingsList: ArrayList<BookingsData>,
) :
RecyclerView.Adapter<WorkerScheduledAdapter.WorkerScheduledViewHolder>() {

    inner class WorkerScheduledViewHolder(val binding: ItemScheduledWorkerBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerScheduledViewHolder {
        return WorkerScheduledViewHolder(
            ItemScheduledWorkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WorkerScheduledViewHolder, position: Int) {
        val currentItem = bookingsList[position]
        holder.binding.apply {
            //gets address details from address collection using addressid
            firestore.collection("address").document(currentItem.addressID!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val addressline = documentSnapshot.getString("address")
                        AddressLine.text = addressline
                    } else {
                        // Document does not exist
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }

            val time = timestampToString(currentItem.timeScheduled)
            ServiceBookingWorker.text = currentItem.service
            itemDate2.text = time

            ProblemDescription.text = currentItem.details
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