package com.example.ayosapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivityLoginBinding
import com.example.ayosapp.worker.WorkerMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.pwordEt.text.toString()
            authenticate(email,password)

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
        val uid = currentUser?.uid
        if (currentUser != null) {
            validate(uid)
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun authenticate(email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid
                    validate(uid)
                } else {
                    val exception = task.exception
                    //Log.d(TAG, "Sign-in failed: $exception")
                    Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Invalid login credentials, please try again", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(uid: String?){
        val customersRef = FirebaseFirestore.getInstance().collection("customers")
        customersRef.document(uid!!).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // UID does not exist as a document ID in the "customers" collection
                    // Check if document ID equal to UID has a boolean field "verification" set to true
                    val userRef = FirebaseFirestore.getInstance().collection("worker").document(uid)
                    userRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists() && documentSnapshot.getBoolean("verification") == true) {
                                // Document ID exists in worker collection and has "verification" set to true
                                val intent = Intent(this, WorkerMainActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Document ID equal to UID either does not exist or "verification" is not true
                                Toast.makeText(this, "Your application has not yet been verified", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}