package com.example.ayosapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentContactUsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ContactUsFragment : Fragment() {
    private lateinit var binding:FragmentContactUsBinding
    private lateinit var firebaseAuth: FirebaseAuth
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

        // Click listener for the parent layout
        binding.sendInquiry.setOnClickListener {
            // Navigate back to ProfileFragment
            parentFragmentManager.popBackStack()
        }

        binding.sendInquiryBtn.setOnClickListener {
            val subject = binding.subjectEt.text.toString()
            val body = binding.bodyEt.text.toString()

            if (subject.isNotEmpty() && body.isNotEmpty()) {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:contactayos@gmail.com") // specify recipient email here
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, body)
                }
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            } else {
                Toast.makeText(requireActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}