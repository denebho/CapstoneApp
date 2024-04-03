package com.example.ayosapp.data

import com.google.firebase.Timestamp

data class BookingsData(
    //val image : Int? = 0,
    val booking_id: String? = "",
    val UID: String? = "",
    val workerAssigned : String? = "",
    val addressID : String? = "",
    val details: String? = "",
    val timeScheduled : Timestamp? = Timestamp.now(),
    val service : String? = "",
    val initialPrice : Double?= 0.0,
    val serviceFee : Double? = 0.0,
    val additionalFee : Double? =0.0,
    val totalPrice : Double? = 0.0,
    val paymentStatus : String? = "",
    val status : String? = "",
    val paymentMethod : String? = "",
    val bookingId : String? = "" ) {
}