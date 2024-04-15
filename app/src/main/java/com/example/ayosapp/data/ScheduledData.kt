package com.example.ayosapp.data

import com.google.firebase.Timestamp
import java.io.Serializable

data class ScheduledData(
    val UID: String? = "",
    val workerAssigned : String? = "",
    val addressID : String? = "",
    val details: String? = "",
    val timeScheduled : Timestamp? = Timestamp.now(),
    val service : String? = "",
    val initialPrice : Double?= 0.0,
    val serviceFee : Double? = 0.0,
    val equipmentFee : Double? =0.0,
    val totalPrice : Double? = 0.0,
    val paymentStatus : String? = "",
    val status : String? = "",
    val paymentMethod : String? = "",
    val bookingId : String? = "" ): Serializable