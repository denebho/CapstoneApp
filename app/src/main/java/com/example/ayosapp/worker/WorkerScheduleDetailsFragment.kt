package com.example.ayosapp.worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ayosapp.data.ScheduledData
import com.example.ayosapp.databinding.FragmentWorkerScheduleDetailsBinding

class WorkerScheduleDetailsFragment : Fragment() {
    private lateinit var binding: FragmentWorkerScheduleDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerScheduleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateValues()
    }

    private fun initiateValues(){
        val bookingData: ScheduledData? =
            arguments?.getSerializable("bookingData") as? ScheduledData

        val userId = bookingData?.UID
        val addressID = bookingData?.addressID
        val details = bookingData?.details
        val timeScheduled = bookingData?.timeScheduled
        val service = bookingData?.service
        val paymentStatus = bookingData?.serviceFee
        val status = bookingData?.status
        val paymentMethod = bookingData?.paymentMethod
        val bookingId = bookingData?.bookingId
        binding.statusBar.text = status
        binding.itemService.text = service
        binding.statusBar.text = status
        binding.statusBar.text = status
        binding.statusBar.text = status
        binding.statusBar.text = status
        binding.statusBar.text = status
        binding.statusBar.text = status

        binding.itemCustomer.text = userId
        //TODO GET NAME FROM REALTIME
    }
}