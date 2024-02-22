package com.example.ayosapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Calendar
import java.util.Date

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG: String = SignupActivity::class.java.name

    private lateinit var emailAddEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var retypepasswordEt: EditText
    private lateinit var btnSignUp: Button
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

        binding.signupBtn.setOnClickListener {
            registerUser()
        }
            binding.workerTv.setOnClickListener {
                val url = "https://forms.gle/tCukQeDBLT8eb62E8"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
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
        if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty()) {
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
                                // Store additional user data in the Realtime Database
                                user?.let {
                                    val database = FirebaseFirestore.getInstance()
                                    val userId = user.uid
                                    val userData = HashMap<String, Any>()
                                    userData["userID"] = userId
                                    userData["email"] = email
                                    userData["password"] = password
                                    userData["create_time"] = timeNow

                                    database.collection("user").document(userId).set(userData)
                                        .addOnSuccessListener {
                                            Toast.makeText(this,"User data saved successfully",Toast.LENGTH_SHORT).show()
                                            Log.d(TAG, "User data saved successfully.")

                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Error saving user data: $e")
                                            Toast.makeText(this,"User data saved failed",Toast.LENGTH_SHORT).show()

                                        }
                                    val intent = Intent(this, WelcomeActivity::class.java)
                                    startActivity(intent)
                                }

                            } else {
                                Toast.makeText(
                                    this,
                                    it.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password must contain a ...", Toast.LENGTH_SHORT).show()
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



    data class User(val userID: String?, val email: String? = null,val password: String?,val create_time: Date = Date()) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

    fun writeNewUser(userId: String, email: String, password: String, create_time: Date) {
        val customer = User(userId, email, password, create_time)

    }
}
