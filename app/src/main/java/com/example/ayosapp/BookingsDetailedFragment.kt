package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.adapter.BookingsAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BookingsDetailedFragment : Fragment() {

    private lateinit var bookingsAdapter: BookingsAdapter
    private var bookingsData: BookingsData? = null
    private var dataArrayList = ArrayList<BookingsData>()
    private lateinit var binding: FragmentBookingsDetailedBinding
    private lateinit var recyclerView: RecyclerView

    companion object {
        // Static method to create a new instance of the fragment
        fun newInstance(bookingData: BookingsData): BookingsDetailedFragment {
            val fragment = BookingsDetailedFragment()
            // Pass data to the fragment using arguments
            val args = Bundle().apply {
                putParcelable("bookingData", bookingData)
            }
            fragment.arguments = args
            return fragment
        }
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
                bookingsAdapter = BookingsAdapter(requireContext(), parentFragmentManager, dataArrayList)
                recyclerView.adapter = bookingsAdapter
            }
            .addOnFailureListener { exception ->
                // Handle failure
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
        fetchDataFromFirestore()
    }
}
