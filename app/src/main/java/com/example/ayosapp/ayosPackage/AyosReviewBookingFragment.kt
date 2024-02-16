package com.example.ayosapp.ayosPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentAyosReviewbookingBinding

class AyosReviewBookingFragment : Fragment() {

    private var _binding: FragmentAyosReviewbookingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAyosReviewbookingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val value = bundle?.getString("addressId")

        binding.bookServiceBtn.setOnClickListener {


        }
    }
}