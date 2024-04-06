package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.security.MessageDigest
import java.util.Calendar

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG: String = SignupActivity::class.java.name

    private lateinit var emailAddEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var retypepasswordEt: EditText
    private lateinit var btnSignUp: Button
    private lateinit var firstnameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var phonenumberEt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        emailAddEt = findViewById(R.id.emailAddEt)
        passwordEt = findViewById(R.id.passwordEt)
        retypepasswordEt = findViewById(R.id.retypepasswordEt)
        btnSignUp = findViewById(R.id.signupBtn)
        firstnameEt = findViewById(R.id.firstnameEt)
        lastNameEt = findViewById(R.id.lastnameEt)
        phonenumberEt = findViewById(R.id.phonenumberEt)

        binding.signupBtn.setOnClickListener {
            registerUser()
        }
            binding.workerTv.setOnClickListener {
                val intent = Intent(this, SignupWorkerActivity::class.java)
                startActivity(intent)
//                val url = "https://forms.gle/tCukQeDBLT8eb62E8"
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(url)
//                startActivity(intent)
            }

            binding.backButton.setOnClickListener {
                finish()
                val backIntent = Intent(this, LoginActivity::class.java)
                startActivity(backIntent)
            }
        }

    private fun registerUser(){
        val timeNow = Calendar.getInstance().time
        val email = emailAddEt.text.toString()
        val password = passwordEt.text.toString()
        val retypePassword = retypepasswordEt.text.toString()
        val firstname = firstnameEt.text.toString()
        val lastname = lastNameEt.text.toString()
        val phonenumber = phonenumberEt.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty() &&
            firstname.isNotEmpty() && lastname.isNotEmpty() && phonenumber.isNotEmpty()) {
            if (password == retypePassword) {
                if (isValidPassword(password.trim())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Account created successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Sign in success, update UI with the signed-in user's information
                                val user = firebaseAuth.currentUser
                                // Store user data in the Firestore Database
                                user?.let {
                                    val database = FirebaseFirestore.getInstance()
                                    val dbreal = FirebaseDatabase.getInstance()
                                    val realtime = dbreal.getReference("user")
                                    val userId = user.uid
                                    val name = "$firstname $lastname"
                                    val userData = HashMap<String, Any>()
                                    userData["user_id"] = userId
                                    userData["email"] = email
                                    userData["password"] = hashPassword(password)
                                    userData["first_name"] = firstname
                                    userData["last_name"] = lastname
                                    userData["mobile_number"] = phonenumber
                                    userData["create_time"] = timeNow

                                    database.collection("customers").document(userId).set(userData)
                                        .addOnSuccessListener {
                                            // Store additional user data in the Realtime Database
                                            realtime.child(userId).setValue(UserInfo(userId,email,name))
                                            Toast.makeText(this,"Registered successfully",Toast.LENGTH_SHORT).show()
                                            Log.d(TAG, "User data saved successfully.")
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Error saving user data: $e")
                                            Toast.makeText(this,"Registration failed, please try again",Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Registration failed, please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(TAG,it.exception.toString())
                            }
                        }
                } else {
                    Toast.makeText(this, "Password must be 8 characters, have an uppercase, a lowercase, a number, and a special character", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    data class UserInfo(
        val uid: String? = null,
        val email: String? = null,
        val name: String? = null
    )
}
