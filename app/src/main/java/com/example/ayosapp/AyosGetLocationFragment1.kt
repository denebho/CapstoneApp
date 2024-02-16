package com.example.ayosapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        //Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ayos_get_location, container, false)
        binding = FragmentAyosGetLocationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val value = bundle?.getInt("addressId").toString()
        binding.addressGroup.setOnClickListener{
            val nextFragment = AddressFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_ayos, nextFragment)
                .addToBackStack(null)
                .commit()

            //addFragmentOnTop(nextFragment)
        }
        if (value.isNotEmpty() && value.isNotBlank()) {
            binding.addressEt.setText(value)
        }

        binding.bookServiceBtn.setOnClickListener{
            arguments = Bundle().apply {
                putString("addressId", value)
                putString("addressString", "address")
                val nextFragment = AyosEnterDetailsFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container_ayos, nextFragment)
                    .addToBackStack(null)
                    .commit()
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