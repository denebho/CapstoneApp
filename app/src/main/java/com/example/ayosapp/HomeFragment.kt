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
import com.example.ayosapp.adapter.HomeAdapter
import com.example.ayosapp.ayosPackage.AyosGetLocationFragment
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private var dataArrayList = ArrayList<BookingsData>()
    private var bookingsData: BookingsData? = null
    private lateinit var recyclerView: RecyclerView

    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerview)
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        dataArrayList = arrayListOf()
        val bundle = Bundle()
        fetchDataFromFirestore()
        binding.homeApplianceIv.setOnClickListener{
            val nextFragment = AyosGetLocationFragment()
            bundle.putString("serviceCode", "Appliance")
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_main, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
        }
        binding.homeElectricIv.setOnClickListener{
            val nextFragment = AyosGetLocationFragment()
            bundle.putString("serviceCode", "Electrical")
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_main, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
        }
        binding.homePlumbingIv.setOnClickListener{
            val nextFragment = AyosGetLocationFragment()
            bundle.putString("serviceCode", "Plumbing")
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_main, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
        }
        binding.homeAirconIv.setOnClickListener{
            val nextFragment = AyosGetLocationFragment()
            bundle.putString("serviceCode", "Aircon")
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_main, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
        }


    }
    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val addressRef = db.collection("booking")
        val bundle = arguments
        addressRef.whereEqualTo("UID", userId).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dataArrayList.add(document.toObject(BookingsData::class.java))
                }
                val adapter = HomeAdapter(requireActivity(),dataArrayList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}