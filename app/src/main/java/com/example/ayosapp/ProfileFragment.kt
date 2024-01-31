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


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var isLoggedIn: Boolean = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        ///val bundle = arguments
        //val message = bundle!!.getString("mText")

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
            val contactUsIntent = Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "contactayos@gmail.com", null))
            startActivity(Intent.createChooser(contactUsIntent, "Send email...."))
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
            isLoggedIn = false
            sendLoginToListener()
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
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }



    fun sendLoginToListener(): Boolean {
        var listener: ((isLoggedIn: Boolean) -> Unit)? = null
        listener?.invoke(isLoggedIn)
        return isLoggedIn

    }
    private fun logoutSession(){
        val bundle = Bundle()
        bundle.putString("isLoggedIn", "false")
    }
}