package com.example.ayosapp.ayosPackage

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentAyosEnterdetailsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AyosEnterDetailsFragment : Fragment() {

    private var _binding: FragmentAyosEnterdetailsBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private lateinit var btnDatePicker: EditText
    private var timepicked: Boolean = false
    private var jobpicked: Boolean = false

    private val applianceOptions: List<String> = listOf("TV", "Refrigerator", "Electric Fan", "Others")
    private val electricalOptions: List<String> = listOf("Lighting", "Circuit Breaker", "Plug Socket", "Others")
    private val airconOptions: List<String> = listOf("Cleaning", "Inspection", "Draining", "Others")
    private val plumbingOptions: List<String> = listOf("Sink", "Shower", "Toilet", "Others")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAyosEnterdetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val service = bundle?.getString("serviceCode")
        val addressid = bundle?.getString("addressid")
        val addressline = bundle?.getString("addressline")
        var selectedTime: String? = null
        var selectedJob: String? = null
        var jobOptions: List<String> = listOf("")

        val icon = binding.serviceIconAyos
        val type = binding.serviceType
        when (service) {
            "Appliance" -> {
                icon.setImageResource(R.drawable.home_appliance)
                jobOptions = applianceOptions
                type.setText(R.string.ayosAppliance)
            }

            "Electrical" -> {
                icon.setImageResource(R.drawable.home_electrical)
                jobOptions = electricalOptions
                type.setText(R.string.ayosElectrical)
            }

            "Plumbing" -> {
                icon.setImageResource(R.drawable.home_plumbing)
                jobOptions = plumbingOptions
                type.setText(R.string.ayosPlumbing)
            }

            "Aircon" -> {
                icon.setImageResource(R.drawable.home_aircon)
                jobOptions = airconOptions
                type.setText(R.string.ayosAircon)
            }
        }

        btnDatePicker = view.findViewById(R.id.dateofserviceEt)
        btnDatePicker.keyListener = null
        btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        binding.bookServiceBtn.setOnClickListener {
            //if (binding.dateofserviceEt.text.isNotEmpty() && binding.autoCompleteID.text.isNotEmpty()){
            val nextFragment = AyosReviewBookingFragment()
            val job = binding.autoCompleteID1.text.toString()
            val detail = binding.detailsEt.text.toString()
            val details = "$job: $detail"
            bundle?.putString("serviceCode", service)
            bundle?.putString("addressid", addressid)
            bundle?.putString("addressline", addressline)
            bundle?.putString("time", binding.autoCompleteID.text.toString())
            Log.d("timepicker", binding.autoCompleteID.text.toString())
            bundle?.putString("date", binding.dateofserviceEt.text.toString())
            Log.d("details", details)
            bundle?.putString("details", details)
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_ayos, nextFragment)
                .addToBackStack(null)
                .commit()
            //}else{
            //    Toast.makeText(requireActivity(), "Please select a time and date to proceed.", Toast.LENGTH_SHORT).show()
            //}
        }

        val timeSlots = mutableListOf<String>()
        val initialTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        for (i in 1 until 16) {
            val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(initialTime.time)
            timeSlots.add(time)
            initialTime.add(Calendar.HOUR_OF_DAY, 1)
        }
        val adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, timeSlots)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val timeSlotSpinner: AutoCompleteTextView = view.findViewById(R.id.autoCompleteID)
        timeSlotSpinner.setAdapter(adapter)

        timeSlotSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedJob = timeSlots[position]
                timepicked = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                timepicked = false
            }
        }
        val adapterJob =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, jobOptions)
        adapterJob.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val jobSpecificationSpinner: AutoCompleteTextView = view.findViewById(R.id.autoCompleteID1)
        jobSpecificationSpinner.setAdapter(adapterJob)

        jobSpecificationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedJob = timeSlots[position]
                    jobpicked = true
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    jobpicked = false
                }
            }

    }


    private fun showDatePicker() {
        // Get tomorrow's date
        val tomorrowCalendar = Calendar.getInstance()
        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrowYear = tomorrowCalendar.get(Calendar.YEAR)
        val tomorrowMonth = tomorrowCalendar.get(Calendar.MONTH)
        val tomorrowDay = tomorrowCalendar.get(Calendar.DAY_OF_MONTH)
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireActivity(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                btnDatePicker.setText(formattedDate)
            },
            tomorrowYear, tomorrowMonth, tomorrowDay
        )
        datePickerDialog.datePicker.minDate = tomorrowCalendar.timeInMillis
        datePickerDialog.show()
    }

}