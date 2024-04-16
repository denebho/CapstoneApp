package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentReportBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReportFragment : Fragment() {
    private lateinit var binding: FragmentReportBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    companion object {
        // Factory method to create a new instance of ReportFragment
        fun newInstance(bookingData: BookingsData): ReportFragment {
            val fragment = ReportFragment()
            // Pass any necessary data to the fragment using arguments
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val collectionRef = db.collection("complaint")

        val ticketTagSpinner = view.findViewById<Spinner>(R.id.ticketTagSpinner)
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.ticket_tags, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ticketTagSpinner.adapter = adapter

        binding.sendReportBtn.setOnClickListener {
            val title = binding.subjectEt.text.toString()
            val body = binding.bodyEt.text.toString()
            val selectedTag = ticketTagSpinner.selectedItem.toString()

            if (title.isNotEmpty() && body.isNotEmpty()) {
                val complaint = hashMapOf(
                    "ticket_title" to title,
                    "ticket_tag" to selectedTag,
                    "ticket_body" to body
                )

                collectionRef.add(complaint)
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Complaint submitted successfully", Toast.LENGTH_SHORT).show()
                        binding.subjectEt.text.clear()
                        binding.bodyEt.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireActivity(), "Error submitting complaint: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

}