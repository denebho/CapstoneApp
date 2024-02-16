package com.example.ayosapp.ayosPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentAyosEnterdetailsBinding
import com.example.ayosapp.databinding.FragmentAyosReviewbookingBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AyosEnterDetailsFragment : Fragment() {

private var _binding: FragmentAyosEnterdetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentAyosEnterdetailsBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val value = bundle?.getString("addressId")

        binding.bookServiceBtn.setOnClickListener {

            val nextFragment = AyosReviewBookingFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container_ayos, nextFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}