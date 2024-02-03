package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var newRecyclerView: RecyclerView
    private var newArrayList = ArrayList<BookingsData>()

    lateinit var imageList : Array<Int>
    lateinit var bookingList : Array<Double>
    lateinit var serviceList : Array<Double>
    lateinit var extraDayList : Array<Double>
    lateinit var totalPriceList : Array<Double>
    lateinit var categoryList : Array<String>
    lateinit var statusList : Array<String>
    lateinit var paymentList : Array<String>
    lateinit var paymentStatusList : Array<String>
    lateinit var workerList : Array<String>
    lateinit var addressList : Array<String>
    lateinit var dateList : Array<String>
    lateinit var bookingIdList : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeApplianceIv.setOnClickListener{
            //insert value
            //gotogetlocationfrag(value)
        }
        binding.homeElectricIv.setOnClickListener{

        }
        binding.homePlumbingIv.setOnClickListener{

        }
        binding.homeAirconIv.setOnClickListener{

        }


        imageList = arrayOf(R.drawable.home_aircon, R.drawable.home_appliance, R.drawable.home_electrical, R.drawable.home_plumbing)

        bookingList = arrayOf(39.00, 49.00, 59.00, 69.00)
        serviceList = arrayOf(500.00, 750.00, 1000.00, 1250.00)
        extraDayList = arrayOf(100.00, 200.00, 300.00, 400.00)
        totalPriceList = arrayOf(539.00, 759.00, 1059.00, 1689.00)

        categoryList = arrayOf("Ayos Aircon", "Ayos Appliance", "Ayos Electrical", "Ayos Plumbing")
        statusList = arrayOf("Ongoing", "Cancelled", "Ayos Na!", "Ayos Na!")
        paymentList = arrayOf("Cash", "Gcash", "Cash", "Gcash")
        paymentStatusList = arrayOf("Unpaid", "Unpaid", "Paid", "Paid")
        workerList = arrayOf("Jane Doe", "John Doe", "Mark Angelo", "Mary Angelo")
        addressList = arrayOf("203 Bayswater Road, Makati South Hills, Paranaque City", "192 Francisco Street, Guinhawa, Malolos City", "223b Benjamin Street, Mandaluyong City", "Unit 53, Grand Apartment, Makati City")
        dateList = arrayOf("2023/10/20-2023/10/22", "2023/10/23-2023/10/24", "2023/11/15-2023/11/17", "2023/11/05-2023/11/06")
        bookingIdList = arrayOf("ID #1234567890", "ID #0987654321", "ID: #1029384756", "ID: #0192837465")

        newRecyclerView = binding.recyclerview
        newRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<BookingsData>()
        getBookingData()

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    }

    private fun getBookingData() {
        for (i in imageList.indices){
            val homeData = BookingsData(imageList[i], workerList[i], addressList[i], dateList[i], categoryList[i],
                bookingList[i], serviceList[i], extraDayList[i], totalPriceList[i],
                paymentStatusList[i], statusList[i], paymentList[i], bookingIdList[i])
            newArrayList.add(homeData)
        }
        newRecyclerView.adapter = HomeAdapter(newArrayList)
    }
}