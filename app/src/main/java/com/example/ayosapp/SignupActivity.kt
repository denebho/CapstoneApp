package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val email = binding.emailAddEt.text.toString()
            val firstname = binding.firstNameEt.text.toString()
            val lastname = binding.lastNameEt.text.toString()
            val phonenumber = binding.phoneNoEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val retypePassword = binding.retypepasswordEt.text.toString()

            if (email.isNotEmpty() && firstname.isNotEmpty() && lastname.isNotEmpty() && phonenumber.isNotEmpty()
                && password.isNotEmpty() && retypePassword.isNotEmpty()){
                if (password == retypePassword){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task->
                        if (task.isSuccessful){
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            finish()
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }

        /*binding.signupBtn.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }*/
        binding.backButton.setOnClickListener{
            val backIntent = Intent(this, LoginActivity::class.java)
            startActivity(backIntent)
        }

    }
}
