package com.example.ayosapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val fragment = com.example.ayosapp.ProfileFragment()
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var session: LoginPref
    private lateinit var sharedpreferences: SharedPreferences
    private var email: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val currentUser = firebaseAuth.currentUser
        //session = LoginPref(this)
        //session.checkLogin()

        //var user: HashMap<String, String> = session.getUserDetails()
        //var email = user.get(LoginPref.KEY_EMAIL)


        bottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bookings -> {
                    replaceFragment(BookingsFragment())
                    true
                }
                R.id.ayosnow -> {
                    replaceFragment(AyosnowFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())

    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
    /*
    override fun onUserInteraction() {
        if(!fragment.sendLoginToListener()) {
            session.LogoutUser()
        }
        session.checkLogin()
        super.onUserInteraction()
    }*/



}