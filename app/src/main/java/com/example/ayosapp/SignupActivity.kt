package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
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
    private lateinit var retypePasswordEt: EditText
    private lateinit var firstNameEt: EditText
    private lateinit var middleNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var birthDateEt: EditText
    private lateinit var phoneNumberEt: EditText
    private lateinit var landlineNumberEt: EditText
    private lateinit var btnSignUp: Button

    private lateinit var emailVerificationLayout: LinearLayout
    private lateinit var resendVerificationTv: TextView
    private lateinit var backToLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        emailAddEt = findViewById(R.id.emailAddEt)
        passwordEt = findViewById(R.id.passwordEt)
        retypePasswordEt = findViewById(R.id.retypePasswordEt)
        firstNameEt = findViewById(R.id.firstNameEt)
        middleNameEt = findViewById(R.id.middleNameEt)
        lastNameEt = findViewById(R.id.lastnameEt)
        birthDateEt = findViewById(R.id.birthDateEt)
        phoneNumberEt = findViewById(R.id.phoneNumberEt)
        landlineNumberEt = findViewById(R.id.landlineNumberEt)
        btnSignUp = findViewById(R.id.signupBtn)
        emailVerificationLayout = findViewById(R.id.emailVerificationLayout)
        resendVerificationTv = findViewById(R.id.resendVerificationTv)
        backToLoginBtn = findViewById(R.id.backToLoginBtn)

        binding.signupBtn.setOnClickListener {
            registerUser()
        }

        binding.workerTv.setOnClickListener {
            val intent = Intent(this, SignupWorkerActivity::class.java)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            finish()
            val backIntent = Intent(this, LoginActivity::class.java)
            startActivity(backIntent)
        }

        binding.resendVerificationTv.setOnClickListener {
            resendVerificationEmail()
        }

        binding.backToLoginBtn.setOnClickListener {
            finish()
            val backIntent = Intent(this, LoginActivity::class.java)
            startActivity(backIntent)
        }
    }

    private fun registerUser() {
        val timeNow = Calendar.getInstance().time
        val email = emailAddEt.text.toString()
        val password = passwordEt.text.toString()
        val retypePassword = retypePasswordEt.text.toString()
        val firstName = firstNameEt.text.toString()
        val middleName = middleNameEt.text.toString()
        val lastName = lastNameEt.text.toString()
        val birthDate = birthDateEt.text.toString()
        val phoneNumber = phoneNumberEt.text.toString()
        val landlineNumber = landlineNumberEt.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty() &&
            firstName.isNotEmpty() && lastName.isNotEmpty() && birthDate.isNotEmpty()
            && phoneNumber.isNotEmpty() && landlineNumber.isNotEmpty()) {
            if (password == retypePassword) {
                if (isValidPassword(password.trim())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // Show the email verification layout
                                emailVerificationLayout.visibility = View.VISIBLE

                                val user = firebaseAuth.currentUser

                                user?.sendEmailVerification()
                                    ?.addOnSuccessListener {
                                        // Verification email sent successfully
                                        Toast.makeText(
                                            this,
                                            "Verification email sent. Please verify your email before logging in.",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        firebaseAuth.signOut()
                                    }
                                    ?.addOnFailureListener { e ->
                                        // Verification email sending failed
                                        Log.e(TAG, "Verification email sending failed: $e")
                                    }

                                user?.let {
                                    val database = FirebaseFirestore.getInstance()
                                    val dbreal = FirebaseDatabase.getInstance()
                                    val realtime = dbreal.getReference("user")
                                    val userId = user.uid
                                    val name = "$firstName $middleName $lastName"
                                    val userData = HashMap<String, Any>()
                                    userData["user_id"] = userId
                                    userData["email"] = email
                                    userData["password"] = hashPassword(password)
                                    userData["first_name"] = firstName
                                    userData["middle_name"] = middleName
                                    userData["last_name"] = lastName
                                    userData["birth_date"] = birthDate
                                    userData["phone_number"] = phoneNumber
                                    userData["landline_number"] = landlineNumber
                                    userData["create_time"] = timeNow

                                    database.collection("customers").document(userId).set(userData)
                                        .addOnSuccessListener {
                                            // Store additional user data in the Realtime Database
                                            realtime.child(userId).setValue(
                                                SignupActivity.UserInfo(
                                                    userId,
                                                    email,
                                                    name
                                                )
                                            )
                                            Toast.makeText(this,"Registered successfully",Toast.LENGTH_SHORT).show()
                                            Log.d(TAG, "User data saved successfully.")
                                            // Don't launch MainActivity here
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Error saving user data: $e")
                                            Toast.makeText(this,"Registration failed, please try again",Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                // Registration failed
                                Toast.makeText(
                                    this,
                                    "Registration failed, please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(TAG, task.exception.toString())
                            }
                        }
                } else {
                    // Password validation failed
                    Toast.makeText(this, "Password must be 8 characters, have an uppercase, a lowercase, a number, and a special character", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Passwords do not match
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Fields are empty
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resendVerificationEmail() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Verification email resent. Please verify your email before logging in.",
                    Toast.LENGTH_LONG
                ).show()
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Failed to resend verification email: $e")
                Toast.makeText(
                    this,
                    "Failed to resend verification email. Please try again later.",
                    Toast.LENGTH_LONG
                ).show()
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
