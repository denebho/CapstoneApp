package com.example.ayosapp.ayosPackage

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.adapter.AddressAdapter
import com.example.ayosapp.data.AddressData
import com.example.ayosapp.databinding.FragmentAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface DataPassListener {
    fun onDataPassed(data: String)
}
class AddressFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentAddressBinding
    private lateinit var addressAdapter: AddressAdapter
    private var dataArrayList = ArrayList<AddressData>()
    private lateinit var addressData: AddressData
    private lateinit var recyclerView: RecyclerView
    private var dataPassListener: DataPassListener? = null
    private lateinit var db: FirebaseFirestore
    //private var db = Firebase.firestore

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
//        var addressAdapter = AddressAdapter(dataArrayList, object :
//            AddressAdapter.OnItemClickListener {
//            override fun onItemClick(data: AddressData) {
//                // Handle item click event
//            }
//        })
        //recyclerView.adapter = addressAdapter

        fetchDataFromFirestore()

        binding.addAddressbtn.setOnClickListener(){
            val intent = Intent(activity, AyosMap::class.java)
            startActivity(intent)
        }


//        binding.addressList.setOnItemClickListener(){parent, view, position, id ->
//            val selectedItem = list.getItemAtPosition(position) as String
//        }


//
    }
    fun setDataPassListener(listener: DataPassListener) {
        this.dataPassListener = listener
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val addressRef = db.collection("address")

        addressRef.whereEqualTo("UID", userId).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    dataArrayList.add(document.toObject(AddressData::class.java))
                }
                val adapter = AddressAdapter(dataArrayList, requireActivity())
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


    }




    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        requireActivity().supportFragmentManager.popBackStack()
//        val previousFragment = requireActivity().supportFragmentManager.findFragmentByTag("getLocationFrag") as? PreviousFragment
//        previousFragment?.setData(dataArrayList)
//        requireActivity().supportFragmentManager.popBackStack()

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