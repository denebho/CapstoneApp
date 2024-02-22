package com.example.ayosapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.databinding.ActivityWelcomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var button: Button
    private lateinit var btnDatePicker: EditText
    private val calendar = Calendar.getInstance()
    private lateinit var database: DatabaseReference
    //private lateinit var session: LoginPref
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG: String = WelcomeActivity::class.java.name
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        database = Firebase.database.reference
        //session = LoginPref(this)
        //session.checkLogin()
        /*
        if(session.isLoggedIn()){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }*/

        val user = Firebase.auth.currentUser
        //val timeNow = Calendar.getInstance()

        btnDatePicker=findViewById(R.id.dateofBirth_et)
        btnDatePicker.keyListener = null
        btnDatePicker.setOnClickListener {
            // Show the DatePicker dialog
            showDatePicker()
        }

        button = findViewById(R.id.saveDetailsBtn)
        button.setOnClickListener {
            val firstname = binding.firstNameEt.text.toString()
            val lastname = binding.lastNameEt.text.toString()
            val phonenumber = binding.phoneNoEt.text.toString()
            val dateofBirth = binding.dateofBirthEt.text.toString()
            val dob = Date(dateofBirth)
            if(firstname.isNotEmpty() && lastname.isNotEmpty() && phonenumber.isNotEmpty() && dateofBirth.isNotEmpty()){
                val user = firebaseAuth.currentUser
                val userId = user!!.uid // Provide the user ID you want to update
                val userRef = db.collection("user").document(userId)
                val query = db.collection("user").whereEqualTo("userID", userId)

                query.get().addOnSuccessListener {
                    val updateMap = mapOf(
                        "first_name" to firstname,
                        "last_name" to lastname,
                        "date_of_birth" to dateofBirth,
                        "mobile_number" to phonenumber
                    )

                    db.collection("user").document(userId).update(updateMap)
                    Toast.makeText(this, "Successfully edited", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"User data save failed",Toast.LENGTH_SHORT).show()

                }
/**
                if (user != null){
                user.let {
                    val database = FirebaseFirestore.getInstance()
                    val userData = HashMap<String, Any>()
                    userData["first_name"] = firstname
                    userData["last_name"] = lastname
                    userData["phone_number"] = phonenumber
                    userData["birth_date"] = dob

                    userRef.update(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, "User data saved successfully.")

                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error saving user data: $e")
                            Toast.makeText(this, "User data saved failed", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }*/
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)


            } else{
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                btnDatePicker.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
    data class User(val userID: String?, val email: String? = null,val last_name: String, val first_name: String, val birth_date: Date, val phone_number: String) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }
    fun writeNewUser(userId: String, email: String?, last_name: String, first_name: String, birth_date: Date, phone_number: String) {
        val customer =
            User(userId, email, last_name, first_name, birth_date, phone_number)
        database.child("customers").child(userId).setValue(customer)
    }


}