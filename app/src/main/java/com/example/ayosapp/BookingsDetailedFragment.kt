package com.example.ayosapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.adapter.BookingsDetailedAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BookingsDetailedFragment : Fragment(), BookingsDetailedAdapter.ReportClickListener {

    private lateinit var bookingsDetailedAdapter: BookingsDetailedAdapter
    private var bookingsData: BookingsData? = null
    private var dataArrayList = ArrayList<BookingsData>()
    private lateinit var binding: FragmentBookingsDetailedBinding
    private lateinit var recyclerView: RecyclerView

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val bookRef = db.collection("booking")
        bookRef.whereEqualTo("UID", userId)
            .orderBy("timeScheduled", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dataArrayList.add(document.toObject(BookingsData::class.java))
                }
                bookingsDetailedAdapter = BookingsDetailedAdapter(requireContext(), dataArrayList, this)
                recyclerView.adapter = bookingsDetailedAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.bookingsDetailedRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingsDetailedAdapter = BookingsDetailedAdapter(requireContext(), dataArrayList, this)
        recyclerView.adapter = bookingsDetailedAdapter
        fetchDataFromFirestore()
    }

    override fun onReportClick(bookingData: BookingsData) {
        val fragment = ReportFragment.newInstance(bookingData)
        parentFragmentManager.beginTransaction()
            .replace(R.id.bookingFillerFragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}
