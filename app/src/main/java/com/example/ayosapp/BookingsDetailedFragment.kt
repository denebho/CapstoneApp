package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.adapter.BookingsDetailedAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentBookingsDetailedBinding

class BookingsDetailedFragment : Fragment(), BookingsDetailedAdapter.ReportClickListener {

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
        binding.reportBtn.setOnClickListener {
            val fragment = ReportFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.bookingFillerFragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onReportClick(bookingData: BookingsData) {
        val fragment = ReportFragment.newInstance(bookingData)
        parentFragmentManager.beginTransaction()
            .replace(R.id.bookingFillerFragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}
