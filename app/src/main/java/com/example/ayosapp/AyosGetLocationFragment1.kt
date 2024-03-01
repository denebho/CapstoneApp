package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.ayosPackage.AddressFragment
import com.example.ayosapp.ayosPackage.AyosBookingActivity
import com.example.ayosapp.ayosPackage.AyosEnterDetailsFragment
import com.example.ayosapp.databinding.FragmentAyosGetLocationBinding


class AyosGetLocationFragment1 : Fragment() {

    private lateinit var binding: FragmentAyosGetLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAyosGetLocationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val addressID = bundle?.getString("addressid","").toString()
        val addressline = bundle?.getString("address","").toString()
        binding.addressGroup.setOnClickListener {
            val nextFragment = AddressFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_ayos, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
        }
        if (addressline.isNotEmpty()) {
            //binding.addressEt.setText(value)
        } else {
            //binding.addressEt.text.clear()
        }

        binding.bookServiceBtn.setOnClickListener {
            if (binding.addressEt.text.isNotEmpty()) {
                arguments = Bundle().apply {
                    putString("addressid", addressID)
                    putString("addressString", addressline)
                    val nextFragment = AyosEnterDetailsFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.frame_container_ayos, nextFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    private fun addFragmentOnTop(fragment: Fragment) {
        val fragmentManager = (activity as AyosBookingActivity).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container_ayos, fragment)
        fragmentTransaction.addToBackStack(null) // This line allows you to go back to the previous fragment when pressing the back button
        fragmentTransaction.commit()
    }

}