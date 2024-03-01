package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var session: LoginPref

    var email: String? = null
    var password:kotlin.String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val SHARED_PREFS = "shared_prefs"
        //val EMAIL_KEY = "email_key"
        //var sharedpreferences: SharedPreferences? = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()
        //lateinit var session: LoginPref
        //session = LoginPref(this)
/*
        if (session.isLoggedIn()){
            var i : Intent = Intent(applicationContext, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }*/

        // getting the data which is stored in shared preferences.
        ///sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        // in shared prefs inside get string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        //email = sharedpreferences?.getString("EMAIL_KEY", null)
        //password = sharedpreferences?.getString("PASSWORD_KEY", null)


        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.pwordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        //intent.putExtra("EMAIL_KEY", EMAIL_KEY)
                        //session.createLoginSession(email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpTv.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }
        binding.forgotPassword.setOnClickListener {
            val forgotIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(forgotIntent)
        }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}