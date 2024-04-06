package com.example.ayosapp.ayosPackage

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.adapter.AddressAdapter
import com.example.ayosapp.adapter.AddressAdapter.OnItemClickListener
import com.example.ayosapp.data.AddressData
import com.example.ayosapp.databinding.FragmentAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddressFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentAddressBinding
    private var dataArrayList = ArrayList<AddressData>()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val list: ListView = view.findViewById(R.id.addressList)
        recyclerView = view.findViewById(R.id.addressList)
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        dataArrayList = arrayListOf()
        val bundle = arguments
        val service = bundle?.getString("serviceCode","").toString()

        fetchDataFromFirestore(service)

        binding.addAddress.setOnClickListener(){
            val intent = Intent(activity, AyosMap::class.java)
            startActivity(intent)
        }
    }
    private fun fetchDataFromFirestore(service: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val addressRef = db.collection("address")
        val bundle = arguments
        addressRef.whereEqualTo("UID", userId).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dataArrayList.add(document.toObject(AddressData::class.java))
                }
                val adapter = AddressAdapter(dataArrayList, requireActivity(), service, db)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    override fun onItemClick(position: Int) {
        // Navigate to another fragment using FragmentTransaction
        val fragment = AyosGetLocationFragment()
        // Pass data if needed
        // fragment.arguments = bundle
        Log.d("addressfrag","onitemclick pass")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }




//    fun EventChangeListener(){
//        db = FirebaseFirestore.getInstance()
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        val addressRef = db.collection("andress")
//
//        addressRef.whereEqualTo("UID", userId).
//            addSnapshotListener(object: EventListener<QuerySnapshot>{
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if(error!=null){
//                        Log.e("Firestore error", error.message.toString())
//                        return
//                    }
//                    for (dc: DocumentChange in value?.documentChanges!!){
//                        Log.d("DEBUG", "User: " )
//                        if (dc.type == DocumentChange.Type.ADDED){
//                            dataArrayList.add(dc.document.toObject(AddressData::class.java))
//                            Log.d("DEBUG", "here:${dataArrayList.size}")
//                        }
//
//                    }
//                }
//            })
//        addressAdapter.notifyDataSetChanged()
//        recyclerView.adapter = AddressAdapter(dataArrayList)
//    }
}