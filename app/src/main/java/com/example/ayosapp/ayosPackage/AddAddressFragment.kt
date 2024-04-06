package com.example.ayosapp.ayosPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentAddAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class addAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddAddressBinding
    private var addbtn : Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.confirmBtnMap)?.visibility = View.GONE
        val bundle = arguments
        val location = bundle?.getString("location")
        val lat = bundle?.getDouble("latitude")
        val long = bundle?.getDouble("longitude")
        val instruction = bundle?.getString("instructions")
        val addressid = bundle?.getString("addressid")
        val addressdetails = bundle?.getString("addressdetails")

        val db = FirebaseFirestore.getInstance()
        // Assuming you have a collection reference to your documents
        val collectionRef = db.collection("address")
        // Create a new document with an automatically generated ID
        addbtn = view.findViewById(R.id.addAddressbtn)

        binding.locationTxt.text = location
        if(!addressdetails.isNullOrBlank()){
            binding.detailsEt.setText(addressdetails)
            val btnstring = getString(R.string.editaddress)
            addbtn?.text = btnstring
        }else{
            val btnstring = getString(R.string.addaddress)
            addbtn?.text = btnstring
        }
        if (!instruction.isNullOrBlank()) {
            binding.instructionsEt.setText(instruction)
        }
        //hides confirm location from ayos map act
        //(activity as AyosMap?)?.updateButtonVisibility()

        binding.backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.addAddressbtn.setOnClickListener {
            //check for empty addressid, if empty passes new address details, else updates document
            if(!addressid.isNullOrBlank()) {
                if(binding.detailsEt.text.toString().trim().isNotBlank()) {
                    val details = binding.detailsEt.text.toString()
                    val instructions = binding.instructionsEt.text.toString()
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val newDocRef = collectionRef.document()
                    val documentId = newDocRef.id
                    val timeNow = Calendar.getInstance().time
                    val addressData = hashMapOf(
                        "addressID" to documentId,
                        "UID" to userId,
                        "address" to location,
                        "address_details" to details,
                        "instructions" to instructions,
                        "latitude" to lat,
                        "longitude" to long,
                        "create_time" to timeNow
                    )
                    newDocRef.set(addressData).addOnSuccessListener {
                        Toast.makeText(
                            requireActivity(),
                            "Address successfully added",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().finish()
                    }.addOnFailureListener {

                    }
                }else{
                    Toast.makeText(
                        requireActivity(),
                        "Address Details cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                //if updating document
                if(binding.detailsEt.text.toString().trim().isNotBlank()) {
                    val addressquery = db.collection("user").whereEqualTo("addressID", addressid)
                    val id: String = addressid.toString()
                    val details = binding.detailsEt.text.toString()
                    val instructions = binding.instructionsEt.text.toString()
                    val timeNow = Calendar.getInstance().time
                    addressquery.get().addOnSuccessListener {
                        val updateMap = mapOf(
                            "address" to location,
                            "address_details" to details,
                            "instructions" to instructions,
                            "latitude" to lat,
                            "longitude" to long,
                            "update_time" to timeNow
                        )

                        db.collection("address").document(id).update(updateMap)
                        Toast.makeText(requireActivity(), "Successfully edited", Toast.LENGTH_SHORT)
                            .show()
                        requireActivity().finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            requireActivity(),
                            "Something happened, please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(
                        requireActivity(),
                        "Address Details cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }

}