package com.example.ayosapp.data

import com.google.firebase.Timestamp

data class BookingsData(
    val UID: String? = "",
    val _token: String? = "",
    val workerAssigned : String? = "",
    val addressID : String? = "",
    val details: String? = "",
    val timeScheduled : Timestamp? = Timestamp.now(),
    val timeBooked : Timestamp? = Timestamp.now(),
    val timeUpdated : Timestamp? = Timestamp.now(),
    val service : String? = "",
    val initialPrice : Double?= 0.0,
    val serviceFee : Double? = 0.0,
    val equipmentFee : Double? =0.0,
    val totalPrice : Double? = 0.0,
    val paymentStatus : String? = "",
    val paymentOptions : String? = "",
    val status : String? = "",
    val bookingId : String? = ""
)