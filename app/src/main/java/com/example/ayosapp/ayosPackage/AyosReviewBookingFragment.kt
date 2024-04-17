package com.example.ayosapp.ayosPackage


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ayosapp.MainActivity
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentAyosReviewbookingBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AyosReviewBookingFragment : Fragment() {

    private var _binding: FragmentAyosReviewbookingBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val db = FirebaseFirestore.getInstance()
    private val binding get() = _binding!!

    private val paymentOptions: List<String> = listOf("Cash","Card")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAyosReviewbookingBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val bundle = arguments
        val service = bundle?.getString("serviceCode")
        val addressline = bundle?.getString("addressline")
        val addressid = bundle?.getString("addressid")
        val timeNow = Calendar.getInstance().time
        val dateSched = bundle?.getString("date")
        val timeSched = bundle?.getString("time")
        val details = bundle?.getString("details")
        val db = FirebaseFirestore.getInstance()
        // Assuming you have a collection reference to your documents
        val collectionRef = db.collection("booking")

        val dateTime = "$dateSched $timeSched"
        val dateString = "$dateSched at $timeSched"  
        val timestamp = dateTimeStringToTimestamp(dateTime)
        binding.timeScheduled.setText(dateString)
        binding.joblocation.setText(addressline)
        binding.noteToWorker.setText(details)
        val icon = binding.serviceIconAyos
        val type = binding.serviceType
        when (service) {
            "Appliance" -> {
                icon.setImageResource(R.drawable.home_appliance)
                type.setText(R.string.ayosAppliance)
            }
            "Electrical"->{
                icon.setImageResource(R.drawable.home_electrical)
                type.setText(R.string.ayosElectrical)
            }
            "Plumbing"->{
                icon.setImageResource(R.drawable.home_plumbing)
                type.setText(R.string.ayosPlumbing)
            }
            "Aircon"->{
                icon.setImageResource(R.drawable.home_aircon)
                type.setText(R.string.ayosAircon)
            }
        }
        val serviceRef = db.collection("service_listings")
        serviceRef.whereEqualTo("service_offered", service).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val iPrice = data["initial_price"]?.toString()
                val setprice = binding.initalPrice
                setprice.setText(iPrice)
            }
        }


        val adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, paymentOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val paymentSpinner: AutoCompleteTextView = view.findViewById(
            R.id.autoCompleteTextView)
        paymentSpinner.setAdapter(adapter)
        paymentSpinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //val selectedTime = paymentOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.bookServiceBtn.setOnClickListener {
            if (binding.autoCompleteTextView.text.isNotBlank()) {
                val user = firebaseAuth.currentUser
                val userId = user?.uid

                user?.let {
                    val iPrice = binding.initalPrice.text.toString()
                    val paymentMethod = paymentSpinner.text.toString()

                    if (paymentMethod.isBlank()) {
                        Toast.makeText(
                            activity,
                            "Please select a payment method",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    val newDocRef = collectionRef.document()
                    val documentId = newDocRef.id
                    val bookingData = hashMapOf(
                        "bookingId" to documentId,
                        "UID" to userId,
                        "service" to service,
                        "timeBooked" to timeNow,
                        "timeScheduled" to timestamp,
                        "addressID" to addressid,
                        "details" to details,
                        "initialPrice" to iPrice.toDouble(),
                        "serviceFee" to 0.00,
                        "equipmentFee" to 0.00,
                        "status" to "booked",
                        "timeUpdated" to timeNow,
                        "workerAssigned" to ""
                    )
                    newDocRef.set(bookingData)
                        .addOnSuccessListener {
                            showDialog()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                activity,
                                "Something went wrong. Please try again",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            Log.e("reviewbooking", "$e")
                        }
                }
            }else{

            }
        }

        binding.cancelServiceBtn.setOnClickListener{
            cancelConfirmationDialog()
        }
    }

    private fun dateTimeStringToTimestamp(dateTimeString: String): Timestamp {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.getDefault())
        val date = dateFormat.parse(dateTimeString)
        return Timestamp(date)
    }
    private fun cancelConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancel Booking")
        builder.setMessage("Are you sure you want to cancel booking?")
        builder.setPositiveButton("YES") { _, _ ->
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ayos_custom_dialog_confirming_booking)
        val btnOk: TextView = dialog.findViewById(R.id.OkBtnDialog)
        btnOk.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
        }
        dialog.show()
    }
}