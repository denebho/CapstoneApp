package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.ItemServicesBinding

class AyosDetailedFragment : Fragment() {

    private lateinit var binding: ItemServicesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val image = args.getInt("image", R.drawable.home_electrical)
            val servicetype = args.getString("service type", "Type")
            val serviceprovider = args.getString("service provider", "Provider")
            val servicedescription = args.getString("description", "Lorem ipsum")

            // Update the UI with detailed information
            binding.serviceIcon.setImageResource(image)
            binding.serviceType.text = servicetype
            binding.serviceProvider.text = serviceprovider
            binding.serviceDescription.text = servicedescription
        }
    }
}