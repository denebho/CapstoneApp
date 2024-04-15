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
import com.example.ayosapp.adapter.BookingsAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentBookingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BookingsFragment : Fragment() {

    private lateinit var bookingsAdapter: BookingsAdapter
    private var bookingsData: BookingsData? = null
    private var dataArrayList = ArrayList<BookingsData>()
    private lateinit var binding: FragmentBookingsBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataArrayList = arrayListOf()
        recyclerView = view.findViewById(R.id.bookingsRv)
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
                val adapter = BookingsAdapter(requireActivity(),dataArrayList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

}