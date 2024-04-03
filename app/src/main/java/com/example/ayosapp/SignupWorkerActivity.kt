package com.example.ayosapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivitySignupWorkerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.Calendar


private const val REQUEST_CODE_IMAGE_PICK = 0
class SignupWorkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupWorkerBinding
    private val TAG: String = SignupWorkerActivity::class.java.name
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var selectedService: BooleanArray
    var serviceOptions: List<String> = listOf("Appliance Repair", "Electrical Repair", "Aircon Repair", "Plumbing Repair")
    var curFile:Uri? = null
    val imageRef = Firebase.storage.reference

    private lateinit var emailAddEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var retypepasswordEt: EditText
    private lateinit var btnSignUp: Button
    private lateinit var firstnameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var phonenumberEt: EditText
    private lateinit var addressEt: EditText
    private lateinit var cityEt: EditText
    private lateinit var serviceEt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivImage.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type ="image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }
        }

        binding.signupBtn.setOnClickListener {
            registerWorker()
            //uploadImageToStorage("myImage")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK){
            data?.data?.let{
                curFile = it
                binding.ivImage.setImageURI(it)
            }
        }
    }
    private fun uploadImageToStorage(filename: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            curFile?.let {
                imageRef.child("images/$filename").putFile(it).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignupWorkerActivity, "Successfully uploaded image",
                        Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SignupWorkerActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun registerWorker(){
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
                                    val userId = user.uid
                                    val userData = HashMap<String, Any>()
                                    userData["user_id"] = userId
                                    userData["email"] = email
                                    userData["password"] = hashPassword(password)
                                    userData["first_name"] = firstname
                                    userData["last_name"] = lastname
                                    userData["mobile_number"] = phonenumber
                                    userData["create_time"] = timeNow
                                    userData["isVerified"] = false

                                    database.collection("customers").document(userId).set(userData)
                                        .addOnSuccessListener {

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
}