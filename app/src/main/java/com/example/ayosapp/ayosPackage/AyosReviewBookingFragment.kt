package com.example.ayosapp.ayosPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentAyosReviewbookingBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AyosReviewBookingFragment : Fragment() {

    private var _binding: FragmentAyosReviewbookingBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val db = FirebaseFirestore.getInstance()
    private val binding get() = _binding!!

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
        val bundle = arguments

        val service = bundle?.getString("sercivetype")
        val timeNow = Calendar.getInstance().time
        val timeSched = bundle?.getString("addressId")
        val address = bundle?.getString("addressId")
        val details = bundle?.getString("details")

        binding.bookServiceBtn.setOnClickListener {
            //TODO pass data from here to database
            // pass booking id, price, address, details, userid, time
            //val user = firebaseAuth.currentUser
            //val userId = user?.uid // Provide the user ID you want to update
            val bookingRef = db.collection("booking").document()

            //if (user != null){
                //user?.let {
                    val database = FirebaseFirestore.getInstance()
                    //get from database
                    val iPrice = null
                    val sFee = null

                    val bookingData = hashMapOf(
                        "UID" to "userId",
                        "service" to service,
                        "timeBooked" to timeNow,
                        "timeScheduled" to timeNow,
                        "addressID" to "address",
                        "details" to "details",
                        "initialPrice" to 500.00,
                        "serviceFee" to 50.00,
                        "additionalFee" to 0.00,
                        "status" to "booked",
                        "timeUpdated" to timeNow,
                    )
                    db.collection("booking").document().set(bookingData)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "User data saved successfully", Toast.LENGTH_SHORT)
                                .show()

                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(activity, "User data saved failed", Toast.LENGTH_SHORT)
                                .show()

                        }
                //}
                //val intent = Intent(this, WelcomeActivity::class.java)
                //startActivity(intent)
            //}
        }

        binding.cancelServiceBtn.setOnClickListener{
            cancelConfirmationDialog()
        }
    }
    private fun cancelConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancel Booking")
        builder.setMessage("Are you sure you want to cancel booking?")
        builder.setPositiveButton("YES") { dialog, _ ->
            // maybe have a bundle eraser
            // start intent -> mainact
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}