package com.example.ayosapp.ayosPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentAyosGetLocationBinding


class AyosGetLocationFragment : Fragment() {

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
        val service = bundle?.getString("serviceCode","").toString()

        val description = binding.serviceDescription
        val type = binding.serviceType
        val icon = binding.serviceIconAyos
        if(service != ""){
            when (service) {
                "Appliance"->{
                    icon.setImageResource(R.drawable.home_appliance)
                    type.setText(R.string.ayosAppliance)
                    description.setText(R.string.appliancedesc)
                }
                "Electrical"->{
                    icon.setImageResource(R.drawable.home_electrical)
                    type.setText(R.string.ayosElectrical)
                    description.setText(R.string.electricaldesc)
                }
                "Plumbing"->{
                    icon.setImageResource(R.drawable.home_plumbing)
                    type.setText(R.string.ayosPlumbing)
                    description.setText(R.string.plumbingdesc)
                }
                "Aircon"->{
                    icon.setImageResource(R.drawable.home_aircon)
                    type.setText(R.string.ayosAircon)
                    description.setText(R.string.aircondesc)
                }
            }
        }

        binding.addressicon.setOnClickListener {
            val nextFragment = AddressFragment()
            bundle?.putString("serviceCode", service)
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_main, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
        }
        binding.addressEt.setOnClickListener {
            val nextFragment = AddressFragment()
            bundle?.putString("serviceCode", service)
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_main, nextFragment, "getLocationFrag")
                .addToBackStack(null)
                .commit()
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