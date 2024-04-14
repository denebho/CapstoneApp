package com.example.ayosapp

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivitySignupWorkerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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

    private lateinit var emailAddEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var retypePasswordEt: EditText
    private lateinit var firstNameEt: EditText
    private lateinit var middleNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var birthDateEt: EditText
    private lateinit var phoneNumberEt: EditText
    private lateinit var landlineNumberEt: EditText
    private lateinit var addressEt: EditText
    private lateinit var cityEt: EditText
    private lateinit var serviceEt: EditText
    private lateinit var clearanceBtn: Button
    private lateinit var btnSignUp: Button

    private lateinit var emailVerificationLayout: LinearLayout
    private lateinit var resendVerificationTv: TextView
    private lateinit var backToLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        emailAddEt = findViewById(R.id.emailAddEt)
        passwordEt = findViewById(R.id.passwordEt)
        retypePasswordEt = findViewById(R.id.retypePasswordEt)
        firstNameEt = findViewById(R.id.firstNameEt)
        middleNameEt = findViewById(R.id.middleNameEt)
        lastNameEt = findViewById(R.id.lastNameEt)
        birthDateEt = binding.birthDateEt
        phoneNumberEt = findViewById(R.id.phoneNumberEt)
        landlineNumberEt = findViewById(R.id.landlineNumberEt)
        addressEt = findViewById(R.id.addressEt)
        cityEt = findViewById(R.id.cityEt)
        selectedService = BooleanArray(serviceOptions.size)
        clearanceBtn = findViewById(R.id.clearanceBtn)
        btnSignUp = findViewById(R.id.signupBtn)
        emailVerificationLayout = findViewById(R.id.emailVerificationLayout)
        resendVerificationTv = findViewById(R.id.resendVerificationTv)
        backToLoginBtn = findViewById(R.id.backToLoginBtn)

        birthDateEt.setOnClickListener {
            showDatePicker()
        }

        binding.clearanceBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
        }

        binding.serviceEt.setOnClickListener {
            showServiceSelectionDialog()
        }

        binding.signupBtn.setOnClickListener {
            uploadBarangayClearance()
        }

        binding.customerTv.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
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

    private fun uploadBarangayClearance() {
        val storageRef = FirebaseStorage.getInstance().reference
        val userId = storageRef.child("barangay_clearance").child("${System.currentTimeMillis()}.jpg")
        if (curFile != null) {
            userId.putFile(curFile!!)
                .addOnSuccessListener { taskSnapshot ->
                    userId.downloadUrl.addOnSuccessListener { uri ->
                        val barangayClearance = uri.toString()
                        registerWorker(barangayClearance)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            // Get the URI of the selected image
            curFile = data?.data
            binding.ivImage.setImageURI(data?.data)
        }
    }

    private fun registerWorker(barangayClearance: String) {
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
            firstName.isNotEmpty() && lastName.isNotEmpty() && birthDate.isNotEmpty() &&
            phoneNumber.isNotEmpty()) {
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

                                    database.collection("worker").document(userId).set(userData)
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

    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, { _, year, monthOfYear, dayOfMonth ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the EditText to display the selected date
                birthDateEt.setText(formattedDate)
            },
            currentYear, currentMonth, currentDay
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }

    private fun showServiceSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Services")
            .setMultiChoiceItems(serviceOptions, selectedService) { _, i, b ->
                if (b) {
                    serviceList.add(i)
                    serviceList.sort()
                } else {
                    serviceList.remove(i)
                }
            }
            .setPositiveButton("OK") { dialogInterface, _ ->
                if (serviceList.isNotEmpty()) {
                    val stringBuilder = StringBuilder()
                    for (j in 0 until serviceList.size) {
                        stringBuilder.append(serviceOptions[serviceList[j]])
                        if (j != serviceList.size - 1) {
                            stringBuilder.append(", ")
                        }
                    }
                    binding.serviceEt.setText(stringBuilder.toString())
                } else {
                    Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setNeutralButton("Clear All") { _, _ ->
                for (j in selectedService.indices) {
                    selectedService[j] = false
                }
                serviceList.clear()
                binding.serviceEt.setText("")
            }
        builder.show()
    }


    fun saveUserDataToFirestore(userId: String?, profileImageUrl: String) {
        // Get reference to Firestore database
        val timeNow = Calendar.getInstance().time
        val email = emailAddEt.text.toString()
        val password = passwordEt.text.toString()
        val firstName = firstNameEt.text.toString()
        val middleName = middleNameEt.text.toString()
        val lastName = lastNameEt.text.toString()
        val birthDate = birthDateEt.text.toString()
        val phoneNumber = phoneNumberEt.text.toString()
        val landlineNumber = landlineNumberEt.text.toString()
        val city = cityEt.text.toString()
        val address = addressEt.text.toString()
        val services = serviceEt.text.toString()
        val db = FirebaseFirestore.getInstance()
        // Create a user object with relevant data
        val userData = HashMap<String, Any>()
        userData["user_id"] = userId.toString()
        userData["email"] = email
        userData["password"] = hashPassword(password)
        userData["first_name"] = firstName
        userData["middle_name"] = middleName
        userData["last_name"] = lastName
        userData["birth_date"] = birthDate
        userData["phone_number"] = phoneNumber
        userData["landline_number"] = landlineNumber
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
