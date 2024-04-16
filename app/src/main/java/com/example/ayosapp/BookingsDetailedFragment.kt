package com.example.ayosapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.adapter.BookingsDetailedAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale

class BookingsDetailedFragment : Fragment(), BookingsDetailedAdapter.ReportClickListener {

    private lateinit var binding: FragmentBookingsDetailedBinding
    private lateinit var bookingsDetailedAdapter: BookingsDetailedAdapter
    private var bookingsData: BookingsData? = null
    private var dataArrayList = ArrayList<BookingsData>()
    private var itemAddress = ""
    private var itemWorker = ""
    private var itemTotal = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookingId = arguments?.getString("bookingId") ?: ""

        fetchDataFromFirebase(bookingId)

        binding.reportBtn.setOnClickListener {
            val fragment = ReportFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.bookingFillerFragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fetchDataFromFirebase(bookingId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("booking").document(bookingId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val bookingData = documentSnapshot.toObject(BookingsData::class.java)
                    if (bookingData != null) {
                        // Update UI elements with fetched data
                        binding.statusBar.text = bookingData.status

                        getWorkerName()
                        getAddress()

                        val timeScheduled = documentSnapshot.get("timeScheduled") as Timestamp
                        binding.itemDate.text = timestampToString(timeScheduled)

                        binding.itemCategory.text = bookingData.service

                        val totalPrice = bookingData.totalPrice?.plus(bookingData.serviceFee!!)?.plus(bookingData.initialPrice!!)?.plus(bookingData.equipmentFee!!)
                        binding.itemTotal.text = totalPrice.toString()

                        binding.itemPaymentStatus.text = documentSnapshot.get("paymentStatus")?.toString()
                        binding.itemPaymentOptions.text = documentSnapshot.get("paymentOptions")?.toString()
                        binding.bookingId.text = bookingData.bookingId
                    }
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting document: ", exception)
            }
    }


    fun getAddress(){
        Log.d("idk", itemAddress )
        val addressref = Firebase.firestore.collection("address")
        addressref.whereEqualTo("addressID", itemAddress).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.itemAddress.text = document.data["address"]?.toString()
                    Log.d("idk",document.data["address"].toString() )
                }
            }
    }

    fun getWorkerName(){
        Log.d("idk", itemWorker )
        val nameref = Firebase.firestore.collection("worker")
        nameref.whereEqualTo("workerId", itemWorker).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val q = document.data["first_name"]?.toString()
                    val w = document.data["middle_name"]?.toString()
                    val e = document.data["last_name"]?.toString()
                    val qwe= "$q $w $e"
                    binding.itemWorker.text = qwe
                    Log.d("idk",document.data["first_name"].toString() )
                }
            }
    }

    fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
        val date = timestamp?.toDate()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date!!)
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timeString = timeFormat.format(date)

        return "$dateString at $timeString"
    }

    override fun onReportClick(bookingData: BookingsData) {
        val fragment = ReportFragment.newInstance(bookingData)
        parentFragmentManager.beginTransaction()
            .replace(R.id.bookingFillerFragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}
