package com.example.ayosapp.worker

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.adapter.HomeAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentWorkerHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class WorkerHomeFragment : Fragment() {
    private var dataArrayList = ArrayList<BookingsData>()
    private lateinit var binding: FragmentWorkerHomeBinding

    private lateinit var recyclerViewBookings: RecyclerView
    private lateinit var recyclerViewSchedule: RecyclerView

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewBookings = view.findViewById(R.id.bookingsAvailableRV)
        recyclerViewSchedule = view.findViewById(R.id.bookingsScheduledRV)
        dataArrayList = arrayListOf()
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val bookingRef = db.collection("booking")
        val bundle = arguments
        bookingRef.whereEqualTo("status", "booked").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dataArrayList.add(document.toObject(BookingsData::class.java))
                }
                val adapter = HomeAdapter(requireActivity(),dataArrayList)
                recyclerViewBookings.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}