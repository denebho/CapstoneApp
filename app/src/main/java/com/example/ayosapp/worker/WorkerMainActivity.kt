package com.example.ayosapp.worker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ayosapp.HomeFragment
import com.example.ayosapp.ProfileFragment
import com.example.ayosapp.R
import com.example.ayosapp.chat.ChatListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class WorkerMainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_main)
        bottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeWorker -> {
                    replaceFragment(WorkerHomeFragment())
                    true
                }

                R.id.scheduleWorker -> {
                    replaceFragment(WorkerScheduleFragment())
                    true
                }

                R.id.bookingsWorker -> {
                    replaceFragment(WorkerBookingsFragment())
                    true
                }

                R.id.chatWorker -> {
                    replaceFragment(ChatListFragment())
                    true
                }

                R.id.profileWorker -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.worker_main_container, fragment)
            .commit()
    }
}