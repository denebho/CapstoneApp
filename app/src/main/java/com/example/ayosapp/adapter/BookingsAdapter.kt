package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.BookingsDetailedFragment
import com.example.ayosapp.R
import com.example.ayosapp.ReportFragment
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemBookingsBinding
import com.example.ayosapp.databinding.ItemBookingsDetailedBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class BookingsAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val bookingsList: ArrayList<BookingsData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_MASTER = 1
        const val VIEW_TYPE_DETAILED = 2
    }

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MASTER -> {
                val binding = ItemBookingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BookingViewHolder(binding.root)
            }
            VIEW_TYPE_DETAILED -> {
                val binding = ItemBookingsDetailedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DetailedBookingViewHolder(binding.root)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = bookingsList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MASTER -> {
                val bookingViewHolder = holder as BookingViewHolder
                bookingViewHolder.bind(currentItem)
            }
            VIEW_TYPE_DETAILED -> {
                val detailedViewHolder = holder as DetailedBookingViewHolder
                detailedViewHolder.bind(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return bookingsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_MASTER
        //return if (position == 0) VIEW_TYPE_MASTER else VIEW_TYPE_DETAILED
    }

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBookingsBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = bookingsList[position]
                    val fragment = BookingsDetailedFragment.newInstance(currentItem)
                    fragmentManager.beginTransaction().replace(R.id.fragmentDetailed_container, fragment).addToBackStack(null).commit()
                }
            }
        }

        fun bind(bookingData: BookingsData) {
            with(binding) {
                when (bookingData.service) {
                    "Appliance" -> itemImage.setImageResource(R.drawable.home_appliance)
                    "Electrical" -> itemImage.setImageResource(R.drawable.home_electrical)
                    "Aircon" -> itemImage.setImageResource(R.drawable.home_aircon)
                    "Plumbing" -> itemImage.setImageResource(R.drawable.home_plumbing)
                }
                statusBar.text = bookingData.service

                if (bookingData.workerAssigned.isNullOrBlank()) {
                    itemWorker.setText(R.string.matchingworker)
                } else {
                    firestore.collection("booking").document(bookingData.workerAssigned).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val worker1 = documentSnapshot.getString("first_name")
                                val worker2 = documentSnapshot.getString("middle_name")
                                val worker3 = documentSnapshot.getString("last_name")
                                val fullName = "$worker1 $worker2 $worker3"
                                itemWorker.text = fullName
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors
                        }
                }
                val time = timestampToString(bookingData.timeScheduled)
                itemDate.text = time
                itemCategory.text = bookingData.service
            }
        }
    }

    inner class DetailedBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBookingsDetailedBinding.bind(itemView)

        init {
            // Assuming you have a report button inside your item layout
            binding.reportBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = bookingsList[position]
                    val fragment = ReportFragment.newInstance(currentItem)
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
                }
            }
        }

        fun bind(bookingData: BookingsData) {
            with(binding) {
                when (bookingData.service) {
                    "Appliance" -> itemImage.setImageResource(R.drawable.home_appliance)
                    "Electrical" -> itemImage.setImageResource(R.drawable.home_electrical)
                    "Aircon" -> itemImage.setImageResource(R.drawable.home_aircon)
                    "Plumbing" -> itemImage.setImageResource(R.drawable.home_plumbing)
                }
                statusBar.text = bookingData.service

                if (bookingData.workerAssigned.isNullOrBlank()) {
                    itemWorker.setText(R.string.matchingworker)
                } else {
                    firestore.collection("booking").document(bookingData.workerAssigned).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val worker1 = documentSnapshot.getString("first_name")
                                val worker2 = documentSnapshot.getString("middle_name")
                                val worker3 = documentSnapshot.getString("last_name")
                                val fullName = "$worker1 $worker2 $worker3"
                                itemWorker.text = fullName
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors
                        }
                }

                firestore.collection("address").document(bookingData.addressID!!).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val addressline = documentSnapshot.getString("address")
                            itemAddress.text = addressline
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }

                val time = timestampToString(bookingData.timeScheduled)
                itemDate.text = time
                itemCategory.text = bookingData.service

                itemInitPrice.text = bookingData.initialPrice.toString()
                itemServiceFee.text = bookingData.serviceFee.toString()
                itemEquipmentFee.text = bookingData.equipmentFee.toString()

                val totalPrice = (bookingData.initialPrice ?: 0.0) + (bookingData.serviceFee ?: 0.0) + (bookingData.equipmentFee ?: 0.0)
                itemTotal.text = totalPrice.toString()

                itemPaymentStatus.text = bookingData.paymentStatus
                itemPaymentOptions.text = bookingData.paymentOptions
                bookingId.text = bookingData.bookingId
            }
        }
    }

    fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
        val date = timestamp?.toDate()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(date!!)
    }
}
