package com.example.ayosapp.ayosPackage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ayosapp.AyosGetLocationFragment1
import com.example.ayosapp.BookingsDetailedFragment
import com.example.ayosapp.R
import com.example.ayosapp.adapter.AddressAdapter
import com.example.ayosapp.data.AddressData
import com.example.ayosapp.databinding.FragmentAddressBinding

interface DataPassListener {
    fun onDataPassed(data: String)
}
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    private lateinit var addressAdapter: AddressAdapter
    private val dataArrayList = ArrayList<AddressData>()
    private lateinit var addressData: AddressData
    private lateinit var fragmentManager : FragmentManager
    private var dataPassListener: DataPassListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: ListView = view.findViewById(R.id.addressList)
        val enterDetailsFragment = AyosGetLocationFragment1()
        binding.addAddressbtn.setOnClickListener(){
            //TODO go to g maps --select marker location --> enteraddress details --> save
            val intent = Intent(activity, AyosBookingActivity::class.java)
            startActivity(intent)
        }

        val addressID = intArrayOf(1,2,3)
        val address1 = arrayOf("Line 1", "Line 1", "Line 1")
        val address2 = arrayOf("Line 2", "Line 2", "Line 2")
        val fullName = arrayOf("Home", "Work", "Custom")
        val number = arrayOf("Cash", "Gcash", "Cash", "Gcash")

        binding.addressList.setOnItemClickListener(){parent, view, position, id ->
            val selectedItem = list.getItemAtPosition(position) as String
        }


        for (i in addressID.indices) {
            addressData = AddressData(
                address1[i], address2[i], fullName[i], number[i])
            addressData?.let { dataArrayList.add(it) }
        }

        context?.let {
            addressAdapter = AddressAdapter(it, dataArrayList)
            binding.addressList.adapter = addressAdapter
            binding.addressList.isClickable = true

            binding.addressList.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                val bookingsDetailedFragment = BookingsDetailedFragment().apply {
                    arguments = Bundle().apply {
                        putInt("addressId", addressID[i])
                    }
                    //dataPassListener?.onDataPassed(arguments.toString())
                }
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentContainerAddress, enterDetailsFragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }

        }

    }
    fun setDataPassListener(listener: DataPassListener) {
        this.dataPassListener = listener
    }
}