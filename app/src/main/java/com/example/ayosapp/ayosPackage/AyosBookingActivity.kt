package com.example.ayosapp.ayosPackage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.ActivityAyosBookingBinding
import androidx.fragment.app.FragmentTransaction
import com.example.ayosapp.AyosGetLocationFragment1
import com.example.ayosapp.R

class AyosBookingActivity : AppCompatActivity()  {


private lateinit var binding: ActivityAyosBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAyosBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getStringExtra("serviceCode")

        // Retrieve the fragment tag from the intent
        val fragmentTag = intent.getStringExtra("fragmentTag")

        // Display the appropriate fragment based on the fragment tag
        /*
        if (fragmentTag == "fragment_tag_1") {
            displayFragment(AyosGetLocationFragment1())
        } else if (fragmentTag == "fragment_tag_2") {
            displayFragment(AddressFragment())
        }*/
        replaceFragment(AyosGetLocationFragment1())


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