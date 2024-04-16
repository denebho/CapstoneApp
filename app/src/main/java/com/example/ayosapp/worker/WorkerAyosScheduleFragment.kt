package com.example.ayosapp.worker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.adapter.WorkerChargesAdapter
import com.example.ayosapp.data.ChargesData
import com.example.ayosapp.databinding.FragmentWorkerAyosScheduleBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale

class WorkerAyosScheduleFragment : Fragment() {
    private var dataArrayList = ArrayList<ChargesData>()
    private var recyclerViewCharges: RecyclerView? = null
    private lateinit var binding: FragmentWorkerAyosScheduleBinding
    private val applianceOptions: List<String> =
        listOf(
            "Cleaning Fee:500.00",
            "Filter Replacement:1299.00",
            "Coil Replacement:999.00",
            "Motor Replacement:199.00"
        )
    private val electricalOptions: List<String> =
        listOf(
            "Rerouting Fee:300.00",
            "Circuit Breaker:200.00",
            "Plug Socket:250.00",
            "Light Bulb:100.00"
        )
    private val plumbingOptions: List<String> =
        listOf(
            "Unclogging Fee:500.00",
            "Hose Replacement:499.00",
            "Hose Clamp:50.00",
            "PVC Pipe:20.00"
        )
    private val airconOptions: List<String> =
        listOf("Cleaning Fee:1000.00", "Draining Fee:500.00", "Hose Clamp:50.00", "PVC Pipe:20.00")
    var chargesSpinner: AutoCompleteTextView? = null
    private var service: String = "Appliance"

    private lateinit var adapters: WorkerChargesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerAyosScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkerAyosScheduleBinding.bind(view)
        recyclerViewCharges = view.findViewById(R.id.extraChargesRV)
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewCharges?.layoutManager = layoutManager
        //dataArrayList = arrayListOf()
        adapters = WorkerChargesAdapter(requireActivity(), dataArrayList)
        recyclerViewCharges?.adapter = adapters
        var jobOptions: List<String> = listOf("")
        val bundle = arguments
        val bookingId = bundle?.getString("bookingId")
        Log.d("idk", bookingId.toString())
        val userRef = Firebase.firestore.collection("booking")
        userRef.whereEqualTo("bookingId", bookingId).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.statusBar.text = document.data["status"]?.toString()
                    binding.itemService.text = document.data["service"]?.toString()
                    service = document.data["service"].toString()
                    val time: Timestamp = document.data["timeScheduled"] as Timestamp
                    binding.itemDate.text = timestampToString(time)
                    val icon = binding.itemImage

                    when (service) {
                        "Appliance" -> {
                            icon.setImageResource(R.drawable.home_appliance)
                            jobOptions = applianceOptions
                        }

                        "Electrical" -> {
                            icon.setImageResource(R.drawable.home_electrical)
                            jobOptions = electricalOptions
                        }

                        "Plumbing" -> {
                            icon.setImageResource(R.drawable.home_plumbing)
                            jobOptions = plumbingOptions
                        }

                        "Aircon" -> {
                            icon.setImageResource(R.drawable.home_aircon)
                            jobOptions = airconOptions
                        }
                    }
                }
            }
        val service1 = binding.itemService.text.toString()
        when (service1) {
            "Appliance" -> {
                jobOptions = applianceOptions
            }

            "Electrical" -> {
                jobOptions = electricalOptions
            }

            "Plumbing" -> {
                jobOptions = plumbingOptions
            }

            "Aircon" -> {
                jobOptions = airconOptions
            }
        }
        //bundle?.getString("serviceCode", "Appliance")
//        val icon = binding.itemImage
//        val type = binding.itemService
//        //var jobOptions: List<String> = listOf("")
//        when (service) {
//            "Appliance" -> {
//                icon.setImageResource(R.drawable.home_appliance)
//                jobOptions = applianceOptions
//                type.setText(R.string.ayosAppliance)
//            }
//            "Electrical" -> {
//                icon.setImageResource(R.drawable.home_electrical)
//                jobOptions = electricalOptions
//                type.setText(R.string.ayosElectrical)
//            }
//            "Plumbing" -> {
//                icon.setImageResource(R.drawable.home_plumbing)
//                jobOptions = plumbingOptions
//                type.setText(R.string.ayosPlumbing)
//            }
//            "Aircon" -> {
//                icon.setImageResource(R.drawable.home_aircon)
//                jobOptions = airconOptions
//                type.setText(R.string.ayosAircon)
//            }
//        }
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, // Use dropdown item layout
            jobOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        chargesSpinner = view.findViewById(R.id.autoCompleteID)
        chargesSpinner?.setAdapter(adapter)

        binding.addextrachargebtn.setOnClickListener {
            val text = binding.autoCompleteID.text
            if (text.isNotEmpty()) {
                addCharge(text.toString())

            } else {
                Toast.makeText(requireActivity(), "No extra charge selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.ayosnabtn.setOnClickListener {
            updateTotalAmount()
            val combinedString = dataArrayList.joinToString(", ") { chargesData ->
                "${chargesData.charge}:${chargesData.price}"
            }
        }
    }

    private fun addCharge(input: String) {
        val parts = input.split(":")
        dataArrayList.add(ChargesData(parts[0], parts[1].toDouble()))
        Log.d("parts", parts.toString())
        Log.d("parts", adapters.itemCount.toString())
        adapters.notifyItemInserted(dataArrayList.size - 1)
        recyclerViewCharges?.scrollToPosition(dataArrayList.size - 1)
        chargesSpinner?.setSelection(0)
        updateTotalAmount()
    }

    private fun getTotal(): Double {
        var total = 0.0
        for (chargesData in dataArrayList) {
            total += chargesData.price ?: 0.0
        }
        return total
    }

    private fun updateTotalAmount() {
        val totalTextView = requireView().findViewById<TextView>(R.id.totalAmount)
        totalTextView.text = getTotal().toString()
    }

    private fun initiate() {

    }

    fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
        val date = timestamp?.toDate()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date!!)
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timeString = timeFormat.format(date)

        return "$dateString at $timeString"
    }
}