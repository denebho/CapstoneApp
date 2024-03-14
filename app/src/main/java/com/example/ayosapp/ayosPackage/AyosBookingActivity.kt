package com.example.ayosapp.ayosPackage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ayosapp.R
import com.example.ayosapp.databinding.ActivityAyosBookingBinding

class AyosBookingActivity : AppCompatActivity()  {


private lateinit var binding: ActivityAyosBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAyosBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val service = intent.getStringExtra("serviceCode")
        val addressid = intent.getStringExtra("addressid")
        val addressline = intent.getStringExtra("addressline")

        val bundle = Bundle().apply {
            putString("serviceCode", service)
            putString("addressid", addressid)
            putString("addressline",addressline)
        }

        val fragment = AyosEnterDetailsFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_ayos, fragment)
            .commit()
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container_ayos, fragment).commit()
    }
    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_ayos, fragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}