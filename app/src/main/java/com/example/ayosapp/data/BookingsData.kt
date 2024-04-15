package com.example.ayosapp.data

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(UID)
        parcel.writeString(_token)
        parcel.writeString(workerAssigned)
        parcel.writeString(addressID)
        parcel.writeString(details)
        parcel.writeLong(timeScheduled?.toDate()?.time ?: 0)
        parcel.writeLong(timeBooked?.toDate()?.time ?: 0)
        parcel.writeLong(timeUpdated?.toDate()?.time ?: 0)
        parcel.writeString(service)
        parcel.writeDouble(initialPrice ?: 0.0)
        parcel.writeDouble(serviceFee ?: 0.0)
        parcel.writeDouble(equipmentFee ?: 0.0)
        parcel.writeDouble(totalPrice ?: 0.0)
        parcel.writeString(paymentStatus)
        parcel.writeString(paymentOptions)
        parcel.writeString(status)
        parcel.writeString(bookingId)
    }

    companion object CREATOR : Parcelable.Creator<BookingsData> {
        override fun createFromParcel(parcel: Parcel): BookingsData {
            return BookingsData(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readSerializable() as Timestamp?,
                parcel.readSerializable() as Timestamp?,
                parcel.readSerializable() as Timestamp?,
                parcel.readString(),
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
            )
        }

        override fun newArray(size: Int): Array<BookingsData?> {
            return arrayOfNulls(size)
        }
    }

}