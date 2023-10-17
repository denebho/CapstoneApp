package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        button = findViewById(R.id.startBtn)
        button.setOnClickListener {
            onLetsClicked()
        }
    }

    private fun onLetsClicked() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()

    }
}