package com.example.ayosapp.worker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.R
import com.example.ayosapp.ReportFragment
import com.example.ayosapp.data.ScheduledData
import com.example.ayosapp.databinding.FragmentWorkerScheduleDetailsBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale

class WorkerScheduleDetailsFragment : Fragment() {
    private lateinit var binding: FragmentWorkerScheduleDetailsBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var addid:String = ""
    private var uid:String = ""
    private var bookingId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerScheduleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        initiateValues(bundle!!)

        binding.reportBtnWorker.setOnClickListener {
            val nextFragment = ReportFragment()
            val id = binding.bookingId.text
            bundle?.putString("bookingid", id.toString())
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.worker_main_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.ayosBtn.setOnClickListener {
            val nextFragment = WorkerAyosScheduleFragment()
            nextFragment.arguments = bundle
            bookingId = bundle?.getString("bookingId").toString()
            bundle?.putString("bookingId", bookingId)
            //nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.worker_main_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initiateValues(bundle: Bundle){
        val bookingData: ScheduledData? =
            arguments?.getSerializable("bookingData") as? ScheduledData
        bookingId = bundle.getString("bookingId").toString()
        val addressLine = bundle.getString("addressLine")
//        val userId: String? = bookingData?.UID
//        val details = bookingData?.details
//        val timeSchedule = bookingData?.timeScheduled
//        val timeScheduled = timestampToString(timeSchedule)
//        val service = bookingData?.service
//        val paymentStatus = bookingData?.paymentStatus
//        val status = bookingData?.status
//        val paymentMethod = bookingData?.paymentMethod
        val bookid ="BOOKING ID: $bookingId"
        binding.itemAddressLine.text = addressLine
        binding.bookingId.text = bookid
        val userRef = Firebase.firestore.collection("booking")
        userRef.whereEqualTo("bookingId", bookingId).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    binding.statusBar.text = document.data["status"]?.toString()
                    binding.itemService.text = document.data["service"]?.toString()
                    addid = document.data["addressID"].toString()
                    uid = document.data["UID"].toString()
                    val time : Timestamp = document.data["timeScheduled"] as Timestamp
                    binding.itemDate.text = timestampToString(time)
                    binding.itemCategory.text = document.data["details"]?.toString()
                    binding.itemPaymentStatus.text = document.data["paymentStatus"]?.toString()
                    binding.itemPaymentMethod.text = document.data["paymentMethod"]?.toString()
                    updateadd()
                    updatename()
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
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
    private fun updateadd(){
        val addressref = Firebase.firestore.collection("address")
        addressref.whereEqualTo("addressID", addid).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.itemAddressDetail.text = document.data["address_details"]?.toString()
                    Log.d("idk",document.data["address_details"].toString() )
                }
            }
    }
    private fun updatename(){
        Log.d("idk",uid )
        val nameref = Firebase.firestore.collection("customers")
        nameref.whereEqualTo("user_id", uid).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val q = document.data["first_name"]?.toString()
                    val e = document.data["last_name"]?.toString()
                    val qwe= "$q $e"
                    binding.itemCustomer.text = qwe
                    Log.d("idk",document.data["first_name"].toString() )
                }
            }
    }
}