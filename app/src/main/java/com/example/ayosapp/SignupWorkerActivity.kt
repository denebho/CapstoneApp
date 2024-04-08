package com.example.ayosapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivitySignupWorkerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.security.MessageDigest
import java.util.Calendar


private const val REQUEST_CODE_IMAGE_PICK = 0

class SignupWorkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupWorkerBinding
    private val TAG: String = SignupWorkerActivity::class.java.name
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var selectedService: BooleanArray
    private val serviceList = ArrayList<Int>()
    private val serviceOptions =
        arrayOf("Appliance Repair", "Electrical Repair", "Aircon Repair", "Plumbing Repair")
    var curFile: Uri? = null
    var userId = ""
    //var imageRef = Firebase.storage.reference

    private lateinit var emailAddEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var retypepasswordEt: EditText
    private lateinit var firstnameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var phonenumberEt: EditText
    private lateinit var addressEt: EditText
    private lateinit var cityEt: EditText
    private lateinit var serviceEt: EditText
    private lateinit var clearanceEt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailAddEt = findViewById(R.id.emailAddEt)
        passwordEt = findViewById(R.id.passwordEt)
        retypepasswordEt = findViewById(R.id.retypepasswordEt)
        firstnameEt = findViewById(R.id.firstnameEt)
        lastNameEt = findViewById(R.id.lastnameEt)
        phonenumberEt = findViewById(R.id.phonenumberEt)
        addressEt = findViewById(R.id.addressEt)
        cityEt = findViewById(R.id.cityEt)
        serviceEt = findViewById(R.id.serviceEt)

        selectedService = BooleanArray(serviceOptions.size)

        binding.clearanceEt.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }
        }

        binding.serviceEt.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Language")
            builder.setCancelable(false)

            builder.setMultiChoiceItems(serviceOptions, selectedService) { _, i, b ->
                if (b) {
                    serviceList.add(i)
                    serviceList.sort()
                } else {
                    serviceList.remove(i)
                }
            }

            builder.setPositiveButton("OK") { dialogInterface, _ ->
                val stringBuilder = StringBuilder()
                for (j in 0 until serviceList.size) {
                    stringBuilder.append(serviceOptions[serviceList[j]])
                    if (j != serviceList.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                binding.serviceEt.setText(stringBuilder.toString())
                dialogInterface.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

            builder.setNeutralButton("Clear All") { _, _ ->
                for (j in selectedService.indices) {
                    selectedService[j] = false
                }
                serviceList.clear()
                binding.serviceEt.setText("")
            }

            builder.show()
        }

        binding.signupBtn.setOnClickListener {
            val email = emailAddEt.text.toString()
            val password = passwordEt.text.toString()
            val retypePassword = retypepasswordEt.text.toString()
            val firstname = firstnameEt.text.toString()
            val lastname = lastNameEt.text.toString()
            val phonenumber = phonenumberEt.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty() &&
                firstname.isNotEmpty() && lastname.isNotEmpty() && phonenumber.isNotEmpty()
            ) {
                if (password == retypePassword) {
                    if (isValidPassword(password.trim())) {
                        registerUser(email, password)
                    } else {
                        Toast.makeText(
                            this,
                            "Password must be 8 characters, have an uppercase, a lowercase, a number, and a special character",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                curFile = it
                binding.ivImage.setImageURI(it)
            }
        }
    }

    fun registerUser(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful
                    val user = FirebaseAuth.getInstance().currentUser
                    // Proceed to upload profile image
                    uploadProfileImage(user?.uid)
                } else {
                    // User registration failed
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun uploadProfileImage(userId: String?) {
        // Get reference to Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/$userId.jpg")

        // Get image file path from your UI (e.g., using ImagePicker library)

        // Upload image file to Firebase Storage
        imageRef.putFile(curFile!!)
            .addOnSuccessListener { taskSnapshot ->
                // Image upload successful, get download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val profileImageUrl = uri.toString()
                    // Proceed to save user data to Firestore
                    saveUserDataToFirestore(userId, profileImageUrl)
                }
            }
            .addOnFailureListener { exception ->
                // Image upload failed
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveUserDataToFirestore(userId: String?, profileImageUrl: String) {
        // Get reference to Firestore database
        val timeNow = Calendar.getInstance().time
        val email = emailAddEt.text.toString()
        val password = passwordEt.text.toString()
        val firstname = firstnameEt.text.toString()
        val lastname = lastNameEt.text.toString()
        val phonenumber = phonenumberEt.text.toString()
        val city = cityEt.text.toString()
        val address = addressEt.text.toString()
        val services = serviceEt.text.toString()
        val db = FirebaseFirestore.getInstance()
        // Create a user object with relevant data
        val userData = HashMap<String, Any>()
        userData["user_id"] = userId.toString()
        userData["email"] = email
        userData["password"] = hashPassword(password)
        userData["first_name"] = firstname
        userData["last_name"] = lastname
        userData["mobile_number"] = phonenumber
        userData["create_time"] = timeNow
        userData["city"] = city
        userData["address"] = address
        userData["services"] = services
        userData["verification"] = false
        userData["rating"] = 0
        userData["IdRef"] = profileImageUrl

        // Add the user object to Firestore
        db.collection("worker").document(userId!!)
            .set(userData)
            .addOnSuccessListener {
                // User data saved successfully
                Toast.makeText(this, "User registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to next screen or perform any other actions
                firebaseAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { exception ->
                // User data save failed
                Toast.makeText(this, "User data save failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerWorker() {
        val timeNow = Calendar.getInstance().time
        val email = emailAddEt.text.toString()
        val password = passwordEt.text.toString()
        val retypePassword = retypepasswordEt.text.toString()
        val firstname = firstnameEt.text.toString()
        val lastname = lastNameEt.text.toString()
        val phonenumber = phonenumberEt.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty() &&
            firstname.isNotEmpty() && lastname.isNotEmpty() && phonenumber.isNotEmpty()
        ) {
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
                                    var userId = user.uid
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

                                            Toast.makeText(
                                                this,
                                                "Registered successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d(TAG, "User data saved successfully.")
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Error saving user data: $e")
                                            Toast.makeText(
                                                this,
                                                "Registration failed, please try again",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Registration failed, please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(TAG, it.exception.toString())
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Password must be 8 characters, have an uppercase, a lowercase, a number, and a special character",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }
                .firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false
        return true
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }


}