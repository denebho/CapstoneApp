package com.example.ayosapp.ayosPackage

import android.app.DatePickerDialog
import android.os.Bundle
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
        val value = bundle?.getString("addressId")

        btnDatePicker = view.findViewById(R.id.dateofserviceEt)
        btnDatePicker.keyListener = null
        btnDatePicker.setOnClickListener {
            // Show the DatePicker dialog
            showDatePicker()
        }

        binding.bookServiceBtn.setOnClickListener {

            val nextFragment = AyosReviewBookingFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_ayos, nextFragment)
                .addToBackStack(null)
                .commit()
        }

        val timeSlots = mutableListOf<String>()
        val initialTime = Calendar.getInstance()

        for (i in 8 until 23) {
            val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(initialTime.time)
            timeSlots.add(time)
            initialTime.add(Calendar.HOUR_OF_DAY, 1)
        }
        val adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, timeSlots)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val timeSlotSpinner: AutoCompleteTextView = view.findViewById(R.id.timeSlotSpinner)
        timeSlotSpinner.setAdapter(adapter)

        timeSlotSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTime = timeSlots[position]
                // Handle selected time
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }



    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireActivity(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                btnDatePicker.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    //private fun timeSlotPicker
    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}