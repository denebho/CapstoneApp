package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.example.ayosapp.adapter.AyosAdapter
import com.example.ayosapp.data.AyosData
import com.example.ayosapp.databinding.FragmentAyosnowBinding

class AyosnowFragment : Fragment() {
    private lateinit var binding: FragmentAyosnowBinding
    private lateinit var ayosAdapter: AyosAdapter
    private val dataArrayList = ArrayList<AyosData>()
    private lateinit var serviceData: AyosData
    private lateinit var fragmentManager : FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAyosnowBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = intArrayOf(R.drawable.home_aircon, R.drawable.home_appliance, R.drawable.home_electrical, R.drawable.home_plumbing)


        val typeList = arrayOf("Ayos Aircon", "Ayos Appliance", "Ayos Electrical", "Ayos Plumbing")
        val providerList = arrayOf("Kuya Eddie", "Kuya Wil", "Kuya Kuya", "Diko Nalam")
        val descriptionList = arrayOf("Cash", "Gcash", "Cash", "Gcash")


        for (i in imageList.indices) {
            serviceData = AyosData(
                imageList[i], typeList[i], providerList[i], descriptionList[i])
            serviceData?.let { dataArrayList.add(it) }
        }

        context?.let {
            ayosAdapter = AyosAdapter(it, dataArrayList)
            binding.serviceList.adapter = ayosAdapter
            binding.serviceList.isClickable = true

            binding.serviceList.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                val bookingsDetailedFragment = BookingsDetailedFragment().apply {
                    arguments = Bundle().apply {
                        putInt("image", imageList[i])
                        putString("service type", typeList[i])
                        putString("service provider", providerList[i])
                        putString("description", descriptionList[i])
                    }
                }
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, bookingsDetailedFragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }

        }

    }

    private fun serviceClicked(fragment:Fragment){
        //fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

}