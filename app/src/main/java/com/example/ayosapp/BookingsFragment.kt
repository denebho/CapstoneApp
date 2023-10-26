package com.example.ayosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentBookingsBinding

class BookingsFragment : Fragment() {

    private lateinit var binding: FragmentBookingsBinding
    private lateinit var bookingsAdapter: BookingsAdapter
    private val dataArrayList = ArrayList<BookingsData>()
    private lateinit var bookingsData: BookingsData

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
        val categoryList = intArrayOf(R.string.ayosAircon, R.string.ayosAppliance, R.string.ayosElectrical, R.string.ayosPlumbing)
        val statusList = intArrayOf(R.string.statusInProgress, R.string.statusCancelled, R.string.statusAyosNa)
        val paymentList = intArrayOf(R.string.paymentCash, R.string.paymentGcash)
        val paymentStatusList = intArrayOf(R.string.paymentStatusUnpaid, R.string.paymentStatusUnpaid)

        val bookingList = doubleArrayOf(39.00, 49.00, 59.00, 69.00, 79.00)
        val serviceList = doubleArrayOf(500.00, 750.00, 1000.00, 1250.00, 1500.00)
        val extraDayList = doubleArrayOf(100.00, 200.00, 300.00)
        val totalPriceList = doubleArrayOf(539.00, 759.00, 1059.00, 1579.00)

        val workerList = arrayOf("Jane Doe", "John Doe", "Mark Millar", "Mary Millar")
        val addressList = arrayOf("203 Bayswater Road, Makati South Hills, Brgy. San Martin de Porres, Paranaque City", "192 Francisco Street, Guinhawa, Malolos City")
        val dateList = arrayOf("2023/10/20-2023/10/22", "2023/10/23-2023/10/24", "2023/11/15-2023/11/17", "2023/11/05-2023/11/06")
        val bookingIdList = arrayOf("1234567890", "0987654321", "1029384756", "0192837465")

        for (i in imageList.indices) {
            bookingsData = BookingsData(
                imageList[i], workerList[i], addressList[i], dateList[i], categoryList[i],
                bookingList[i], serviceList[i], extraDayList[i], totalPriceList[i],
                paymentStatusList[i], statusList[i], paymentList[i], bookingIdList[i])
            dataArrayList.add(bookingsData)
        }
        bookingsAdapter = BookingsAdapter(requireContext(), dataArrayList)
        binding.listview.adapter = bookingsAdapter
        binding.listview.isClickable = true

        binding.listview.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val bookingsDetailedFragment = BookingsDetailedFragment()

                val args = Bundle()
                args.putInt("image", imageList[i])
                args.putInt("category", categoryList[i])
                args.putInt("status", statusList[i])
                args.putInt("payment method", paymentList[i])
                args.putInt("payment status", paymentStatusList[i])
                args.putDouble("booking fee", bookingList[i])
                args.putDouble("service fee", serviceList[i])
                args.putDouble("extra day fee", extraDayList[i])
                args.putDouble("total fee", totalPriceList[i])
                args.putString("worker", workerList[i])
                args.putString("address", addressList[i])
                args.putString("date", dateList[i])
                args.putString("booking id", bookingIdList[i])
                bookingsDetailedFragment.arguments = args

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.listview, bookingsDetailedFragment)
                    .addToBackStack(null)
                    .commit()
            }
    }
}