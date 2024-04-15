package com.example.ayosapp.worker

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.adapter.WorkerScheduledAdapter
import com.example.ayosapp.data.ScheduledData
import com.example.ayosapp.databinding.FragmentWorkerScheduleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class WorkerScheduleFragment : Fragment(), WorkerScheduledAdapter.ClickListener {
    private var dataArrayList1 = ArrayList<ScheduledData>()
    private lateinit var binding: FragmentWorkerScheduleBinding
    private lateinit var recyclerViewSchedule: RecyclerView
    private lateinit var listener: WorkerScheduledAdapter.ClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listener = this
        binding = FragmentWorkerScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataArrayList1 = arrayListOf()
        val layoutManager1 = LinearLayoutManager(requireActivity())
        recyclerViewSchedule = view.findViewById(R.id.bookingsScheduledRV)
        recyclerViewSchedule.layoutManager = layoutManager1
        fetchScheduledDataFromFirestore()
    }
    private fun fetchScheduledDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val scheduleRef = db.collection("booking")
        scheduleRef.whereEqualTo("workerAssigned", userId)
            .orderBy("timeScheduled", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dataArrayList1.add(document.toObject(ScheduledData::class.java))
                }
                val adapter = WorkerScheduledAdapter(requireActivity(),dataArrayList1,listener)
                recyclerViewSchedule.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    override fun onBookingListItemClick(view: View, bookingId: String, addressLine:String) {
        val nextFragment = WorkerScheduleDetailsFragment()
        val bundle = Bundle()
        bundle.putString("bookingId",bookingId )
//            Bundle().apply {
//           putSerializable("bookingData", user)
//        }
        nextFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.worker_main_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}