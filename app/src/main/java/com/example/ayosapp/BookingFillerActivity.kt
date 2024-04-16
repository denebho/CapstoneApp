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
        val _token = intent.getStringExtra("_token")
        val workerAssigned = intent.getStringExtra("workerAssigned")
        val addressID = intent.getStringExtra("addressID")
        val details = intent.getStringExtra("details")
        val timeScheduled = intent.getStringExtra("timeScheduled")
        val timeBooked = intent.getStringExtra("timeBooked")
        val timeUpdated = intent.getStringExtra("timeUpdated")
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

        if (fragmentTag == "bookingsDetailedFragment")
            replaceFragment(BookingsDetailedFragment())
        supportActionBar?.title = name
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.bookingFillerFragmentContainerView, fragment).commit()
    }
}