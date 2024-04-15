package com.example.ayosapp

import android.content.ContentValues
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

    override fun onReportClick() {
        val nextFragment = ReportFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.bookingDetailed_container, nextFragment)
            .addToBackStack(null)
            .commit()
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
        dataArrayList = arrayListOf()
        recyclerView = view.findViewById(R.id.bookingsDetailedRv)
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        fetchDataFromFirestore()
    }

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
                val adapter = BookingsDetailedAdapter(requireActivity(), parentFragmentManager, dataArrayList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}