package com.example.ayosapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class BookingFillerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_filler)

        val name = null
        val UID = intent.getStringExtra("UID")
        val workerAssigned = intent.getStringExtra("workerAssigned")
        val addressID = intent.getStringExtra("addressID")
        val details = intent.getStringExtra("details")
        val timeScheduled = intent.getStringExtra("timeScheduled")
        val timeBooked = intent.getStringExtra("timeBooked")
        val service = intent.getStringExtra("service")
        val initialPrice = intent.getDoubleExtra("initialPrice", 0.0)
        val serviceFee = intent.getDoubleExtra("serviceFee", 0.0)
        val equipmentFee = intent.getDoubleExtra("equipmentFee", 0.0)
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val paymentStatus = intent.getStringExtra("paymentStatus")
        val paymentOptions = intent.getStringExtra("paymentOptions")
        val status = intent.getStringExtra("status")
        val bookingId = intent.getStringExtra("bookingId")

        val fragmentTag = intent.getStringExtra("fragmentTag")

        if (fragmentTag == "bookingsDetailedFragment") {
            val fragment = BookingsDetailedFragment().apply {
                arguments = Bundle().apply {
                    putString("bookingId", bookingId)
                }
            }
            replaceFragment(fragment)
            supportActionBar?.title = name
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.bookingFillerFragmentContainerView, fragment).commit()
    }
}