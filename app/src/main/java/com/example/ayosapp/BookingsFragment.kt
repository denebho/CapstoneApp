package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.ayosapp.adapter.BookingsAdapter
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.FragmentBookingsBinding

class BookingsFragment : Fragment() {

    private lateinit var binding: FragmentBookingsBinding
    private lateinit var bookingsAdapter: BookingsAdapter
    private val dataArrayList = ArrayList<BookingsData>()
    private var bookingsData: BookingsData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = intArrayOf(R.drawable.home_aircon, R.drawable.home_appliance, R.drawable.home_electrical, R.drawable.home_plumbing)

        val bookingList = doubleArrayOf(39.00, 49.00, 59.00, 69.00)
        val serviceList = doubleArrayOf(500.00, 750.00, 1000.00, 1250.00)
        val extraDayList = doubleArrayOf(100.00, 200.00, 300.00, 400.00)
        val totalPriceList = doubleArrayOf(539.00, 759.00, 1059.00, 1689.00)

        val categoryList = arrayOf("Ayos Aircon", "Ayos Appliance", "Ayos Electrical", "Ayos Plumbing")
        val statusList = arrayOf("Ongoing", "Cancelled", "Ayos Na!", "Ayos Na!")
        val paymentList = arrayOf("Cash", "Gcash", "Cash", "Gcash")
        val paymentStatusList = arrayOf("Unpaid", "Unpaid", "Paid", "Paid")
        val workerList = arrayOf("Jane Doe", "John Doe", "Mark Angelo", "Mary Angelo")
        val addressList = arrayOf("203 Bayswater Road, Makati South Hills, Paranaque City", "192 Francisco Street, Guinhawa, Malolos City", "223b Benjamin Street, Mandaluyong City", "Unit 53, Grand Apartment, Makati City")
        val dateList = arrayOf("2023/10/20-2023/10/22", "2023/10/23-2023/10/24", "2023/11/15-2023/11/17", "2023/11/05-2023/11/06")
        val bookingIdList = arrayOf("ID #1234567890", "ID #0987654321", "ID: #1029384756", "ID: #0192837465")

        for (i in imageList.indices) {
            bookingsData = BookingsData(
                imageList[i], workerList[i], addressList[i], dateList[i], categoryList[i],
                bookingList[i], serviceList[i], extraDayList[i], totalPriceList[i],
                paymentStatusList[i], statusList[i], paymentList[i], bookingIdList[i])
            bookingsData?.let { dataArrayList.add(it) }
        }

        context?.let {
            bookingsAdapter = BookingsAdapter(it, dataArrayList)
            binding.listview.adapter = bookingsAdapter
            binding.listview.isClickable = true

            binding.listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                    val bookingsDetailedFragment = BookingsDetailedFragment().apply {
                        arguments = Bundle().apply {
                            putInt("image", imageList[i])
                            putString("category", categoryList[i])
                            putString("status", statusList[i])
                            putString("payment method", paymentList[i])
                            putString("payment status", paymentStatusList[i])
                            putDouble("booking fee", bookingList[i])
                            putDouble("service fee", serviceList[i])
                            putDouble("extra day fee", extraDayList[i])
                            putDouble("total fee", totalPriceList[i])
                            putString("worker", workerList[i])
                            putString("address", addressList[i])
                            putString("date", dateList[i])
                            putString("booking id", bookingIdList[i])
                        }
                    }
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, bookingsDetailedFragment)
                        ?.addToBackStack(null)
                        ?.commit()
            }

        }

    }
}