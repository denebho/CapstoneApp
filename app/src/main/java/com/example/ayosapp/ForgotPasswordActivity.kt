package com.example.ayosapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivityForgotpasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity: AppCompatActivity() {
    private lateinit var binding: ActivityForgotpasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.resetPasswordBtn.setOnClickListener {
            val email = binding.registeredEmailEt.text.toString().trim() { it <= ' '}
            if (email.isEmpty()) {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please enter your email address.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Email sent successfully to reset your password!",
                                Toast.LENGTH_LONG
                            ).show()

                            finish()
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        binding.backButton.setOnClickListener{
            val backIntent = Intent(this, LoginActivity::class.java)
            startActivity(backIntent)
        }
    }
}