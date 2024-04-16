package com.example.ayosapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ayosapp.chat.ChatFragment

class FillerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filler)
        val workerID = intent.getStringExtra("workerID")
        val name = null
        val data = intent.getStringExtra("serviceCode")
        val receivedIntent = intent
        // Retrieve the fragment tag from the intent
        val fragmentTag = intent.getStringExtra("fragmentTag")

        if (fragmentTag == "chatFragment") {
            replaceFragment(ChatFragment())
            supportActionBar?.title = name

        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fillerFragmentContainerView, fragment).commit()
    }
}