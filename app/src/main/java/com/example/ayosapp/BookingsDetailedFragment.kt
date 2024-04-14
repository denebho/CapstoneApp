package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding

class BookingsDetailedFragment : Fragment() {

    private lateinit var binding: FragmentBookingsDetailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        arguments?.let { args ->
            val image = args.getInt("image", R.drawable.home_electrical)
            val category = args.getString("category", "Category")
            val paymentStatus = args.getString("payment status", "Pay Status")
            val status = args.getString("status", "Status")
            val paymentMethod = args.getString("payment method", "Pay Method")
            val bookingFee = args.getString("booking fee", "39.00")?.toDouble() ?: 0.0
            val serviceFee = args.getString("service fee", "500.00")?.toDouble() ?: 0.0
            val equipmentFee = args.getString("equipment fee", "100.00")?.toDouble() ?: 0.0
            val totalFee = args.getString("total fee", "539.00")?.toDouble() ?: 0.0
            val worker = args.getString("worker", "Worker Name")
            val address = args.getString("address", "Road, Barangay, City")
            val date = args.getString("date", "XX/XX/20XX")
            val bookingId = args.getString("booking id", "#XXXXX")

            binding.itemImage.setImageResource(image)
            binding.itemCategory.text = category
            binding.itemPaymentStatus.text = paymentStatus
            binding.statusBar.text = status
            binding.itemPaymentMethod.text = paymentMethod
            binding.itemBookingFee.text = bookingFee.toString()
            binding.itemServiceFee.text = serviceFee.toString()
            binding.itemEquipmentFee.text = equipmentFee.toString()
            binding.itemTotalFee.text = totalFee.toString()
            binding.itemWorker.text = worker
            binding.itemAddress.text = address
            binding.itemDate.text = date
            binding.bookingId.text = bookingId */

            binding.reportBtn.setOnClickListener {
                val nextFragment = ReportFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.bookingDetailed_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }


        }
    }
}