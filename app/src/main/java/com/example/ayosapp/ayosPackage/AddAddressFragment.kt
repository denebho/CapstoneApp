package com.example.ayosapp.ayosPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ayosapp.databinding.FragmentAddAddressBinding

class addAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddAddressBinding

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_address, container, false)
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)

        val data = arguments?.getString("location")

        binding.locationTxt.text = data

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (binding.detailsEt.text.isNotEmpty()){
            //TODO get both ETs and commit location to database
        }
    }

}