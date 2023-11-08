package com.example.ayosapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.MultiFactorSession

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val SHARED_PREFS = "shared_prefs"
    val EMAIL_KEY = "email_key"
    val PASSWORD_KEY = "password_key"

    var email: String? = null
    var password:kotlin.String? = null

    private lateinit var eUsername: EditText
    private lateinit var eEmail: EditText
    private lateinit var btnLogin: Button
    lateinit var session: LoginPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var sharedpreferences: SharedPreferences? = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        session = LoginPref(this)

        if (session.isLoggedIn()){
            var i : Intent = Intent(applicationContext, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        // getting the data which is stored in shared preferences.
        ///sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        // in shared prefs inside get string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        ///email = sharedpreferences.getString("EMAIL_KEY", null)
        ///password = sharedpreferences.getString("PASSWORD_KEY", null)


        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.pwordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        session.createLoginSession(email)
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
}