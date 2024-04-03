package com.example.ayosapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("user").child(userId)

        userRef.get().addOnSuccessListener {dataSnapshot ->
            if (dataSnapshot.exists()) {
                val userData = dataSnapshot.getValue(userinfo::class.java)
                binding.profileName.text = "${userData?.name}"
                binding.profileEmail.text = "${userData?.email}"
                //Log.w("Chat Fragment", "${userData?.name}")
            } else {
                    // Document doesn't exist for the current user's UID
            }
        }

        binding.personalInfoTv.setOnClickListener {
            val nextFragment = PersonalinfoFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.ayosWorkerTv.setOnClickListener {
            val url = "https://forms.gle/tCukQeDBLT8eb62E8"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.contactUsTv.setOnClickListener {
            val nextFragment = ContactUsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_container, nextFragment)
                .addToBackStack(null)
                .commit()
//            val contactUsIntent = Intent(Intent.ACTION_SENDTO,
//                Uri.fromParts("mailto", "contactayos@gmail.com", null))
//            startActivity(Intent.createChooser(contactUsIntent, "Send email...."))
        }

        binding.aboutUsTv.setOnClickListener {
            val nextFragment = AboutusFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.logOutTv.setOnClickListener {
            logoutConfirmationDialog()
        }

        return binding.root
    }

    private fun logoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("LOG OUT")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("YES") { dialog, _ ->
            //isLoggedIn = false
            //session!!.LogoutUser()
            firebaseAuth.signOut()
            dialog.dismiss()
            navigateToLogin()
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


data class userinfo (
    val name: String?=null,
    val email: String?=null)
}