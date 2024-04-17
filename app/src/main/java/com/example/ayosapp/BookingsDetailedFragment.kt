package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.adapter.BookingsDetailedAdapter
import com.example.ayosapp.adapter.ChargesAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.data.ChargesData
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding
import com.example.ayosapp.payment.PaymentTest
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookingsDetailedFragment : Fragment(), BookingsDetailedAdapter.ReportClickListener {

    private lateinit var binding: FragmentBookingsDetailedBinding
    private var itemAddress = ""
    private var itemWorker = ""
    private var status = ""
    private var timescheduled = Timestamp.now()
    private var dataArrayList = ArrayList<ChargesData>()
    private var recyclerViewCharges: RecyclerView? = null
    private lateinit var adapters: ChargesAdapter
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

        Log.d("DetailedFrag","time is: $timescheduled")
        binding.reportBtn.setOnClickListener {
            val fragment = ReportFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.bookingFillerFragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.cancelBtn1.setOnClickListener {
            confirmDialog(bookingId)
        }
        binding.payBtn.setOnClickListener {
            val intent = Intent(requireContext(),PaymentTest::class.java)
            val total = binding.itemTotal.text.toString()
            val totalAmount = total.toDouble()
            intent.putExtra("totalAmount", totalAmount)
            intent.putExtra("bookingId",bookingId)
            requireContext().startActivity(intent)
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
                        status = bookingData.status.toString()
                        itemWorker = bookingData.workerAssigned.toString()
                        getWorkerName()
                        itemAddress = bookingData.addressID.toString()
                        getAddress()

                        timescheduled = documentSnapshot.get("timeScheduled") as Timestamp
                        binding.itemDate.text = timestampToString(timescheduled)

                        binding.itemCategory.text = bookingData.service

                        val totalPrice = bookingData.totalPrice?.plus(bookingData.serviceFee!!)?.plus(bookingData.initialPrice!!)?.plus(bookingData.equipmentFee!!)
                        binding.itemTotal.text = totalPrice.toString()

                        binding.itemPaymentStatus.text = documentSnapshot.get("paymentStatus")?.toString()
                        binding.itemPaymentOptions.text = documentSnapshot.get("paymentOptions")?.toString()
                        binding.bookingId.text = bookingData.bookingId

                        val servicefee = bookingData.serviceFee
                        val bookingFee = bookingData.initialPrice
                        val extracharges =bookingData.extracharges
                        val charges = "Booking Fee:$bookingFee, Service Fee:$servicefee, $extracharges"
                        val items = charges?.split(", ")
                        items?.forEach { item ->
                            val splitItem = item.split(":")
                            if (splitItem.size == 2) {
                                val charge = splitItem[0]
                                val price = splitItem[1].toDoubleOrNull() ?: 0.0 // Convert price to Double or use default value
                                dataArrayList.add(ChargesData(charge, price))
                            }
                        }
                        recyclerViewCharges = view?.findViewById(R.id.extraChargesRV1)
                        val layoutManager = LinearLayoutManager(requireActivity())
                        recyclerViewCharges?.layoutManager = layoutManager
                        //dataArrayList = arrayListOf()
                        adapters = ChargesAdapter(requireActivity(), dataArrayList)
                        recyclerViewCharges?.adapter = adapters
                        // Calculate the difference between timeScheduled and currentTime in milliseconds
                        val cancelBtn = requireView().findViewById<Button>(R.id.cancelBtn1)
                        val payBtn = requireView().findViewById<Button>(R.id.payBtn)
                        val currentTime = Timestamp.now()
                        val timeDifferenceInMillis = timescheduled.seconds * 1000 - currentTime.seconds * 1000
                        Log.d("DetailedFrag","timeDifferenceInMillis is: $timeDifferenceInMillis")

                        //Calculate the difference in hours
                        val hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis)
                        Log.d("DetailedFrag","hoursDifference is: $hoursDifference")

                        //Set the color of cancelBtn based on the time difference
                        if (hoursDifference >= 12) {
                            // More than 12 hours before local time
                            cancelBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_blue)) // Set your desired color
                            cancelBtn.isClickable = true
                        } else {
                            // Less than 12 hours before local time
                            cancelBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black)) // Set your desired color
                            cancelBtn.isClickable = false
                        }
                        val paymentstat = bookingData.paymentStatus

                        if (paymentstat == "paid"){
                            payBtn.visibility = View.GONE
                        }else{
                            payBtn.visibility = View.VISIBLE
                        }
                        when (status) {
                            "cancelled" -> {
                                cancelBtn.visibility = View.GONE
                                payBtn.visibility = View.GONE
                            }
                            "ayos na" -> {
                                cancelBtn.visibility = View.GONE
                            }
                            else -> {
                                // Default case when status is neither "paid" nor "cancelled"
                                cancelBtn.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting document: ", exception)
            }
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Booking Cancelled")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun confirmDialog(bookingId: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancelling Booking")
        builder.setMessage("Are you sure you want to cancel this booking done?")
        builder.setPositiveButton("YES") { _, _ ->
            cancelBooking(bookingId)
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun cancelBooking(bookingId:String){
        val userRef = Firebase.firestore.collection("booking")
        userRef.whereEqualTo("bookingId", bookingId).get()
        val updateMap = mapOf(
            "status" to "cancelled",
        )
        Firebase.firestore.collection("booking").document(bookingId).update(updateMap)
        showDialog()
    }

    private fun getAddress(){
        val addressref = Firebase.firestore.collection("address")
        addressref.whereEqualTo("addressID", itemAddress).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.itemAddress.text = document.data["address"]?.toString()
                    Log.d("idk",document.data["address"].toString() )
                }
            }
    }

    private fun getWorkerName(){
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

    private fun timestampToString(timestamp: Timestamp?): String {
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
