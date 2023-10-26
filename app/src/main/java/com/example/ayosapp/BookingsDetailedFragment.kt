package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding

class BookingsDetailedFragment : Fragment() {

    private var _binding: FragmentBookingsDetailedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingsDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image = requireArguments().getInt("image")
        val category = requireArguments().getInt("category")
        val paymentStatus = requireArguments().getInt("payment status")
        val status = requireArguments().getInt("status")
        val paymentMethod = requireArguments().getInt("payment method")
        val bookingFee = requireArguments().getDouble("booking fee")
        val serviceFee = requireArguments().getDouble("service fee")
        val extraDayFee = requireArguments().getDouble("extra day fee")
        val totalFee = requireArguments().getDouble("total fee")
        val worker = requireArguments().getString("worker")
        val address = requireArguments().getString("address")
        val date = requireArguments().getString("date")
        val bookingId = requireArguments().getString("booking id")

        // Update the UI with detailed information
        binding.itemImage.setImageResource(image)
        binding.itemCategory.text = getString(category)
        binding.itemPaymentStatus.text = getString(paymentStatus)
        binding.statusBar.text = getString(status)
        binding.itemPaymentMethod.text = getString(paymentMethod)
        binding.itemBookingFee.text = bookingFee.toString()
        binding.itemServiceFee.text = serviceFee.toString()
        binding.itemExtradayFee.text = extraDayFee.toString()
        binding.itemTotalFee.text = totalFee.toString()
        binding.itemWorker.text = worker
        binding.itemAddress.text = address
        binding.itemDate.text = date
        binding.bookingId.text = bookingId
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}