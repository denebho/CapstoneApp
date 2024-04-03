package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentContactUsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class ContactUsFragment : Fragment() {
    private lateinit var binding:FragmentContactUsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val collectionRef = db.collection("complaint")


        binding.sendComplaintBtn.setOnClickListener {
            val subject = binding.subjectEt.text.toString()
            val body = binding.bodyEt.text.toString()
            val timeNow = Calendar.getInstance().time

            if (subject.isNotEmpty() && body.isNotEmpty())
            {
                val user = firebaseAuth.currentUser
                val userId = user?.uid
                val newDocRef = collectionRef.document()
                val documentId = newDocRef.id
                //get document id and pass as complaint id
                user?.let {
                    //val iPrice = binding.initalPrice.text.toString()
                    val complaintData = hashMapOf(
                        "uid" to userId,
                        "report_subject" to subject,
                        "report_body" to body,
                        "create_time" to timeNow,
                        "status" to "open",
                        "updated_by" to "",
//                        report_id STRING
//                                report_subject STRING
//                                report_body STRING
//                                uid STRING
//                                status STRING
//                                create_time timestamp
//                                update_time timestamp
//                                updated_by STRING
                    )
                    db.collection("complaint").document().set(complaintData)
                        .addOnSuccessListener {
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                            Toast.makeText(activity, "Service Booked!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                activity,
                                "Something went wrong. Please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("reviewbooking", "$e")
                        }
                }
            }else{
                Toast.makeText(requireActivity(),"Fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
}