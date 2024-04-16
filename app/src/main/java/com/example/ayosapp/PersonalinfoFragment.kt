package com.example.ayosapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentPersonalinfoBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PersonalinfoFragment : Fragment() {

    private lateinit var binding: FragmentPersonalinfoBinding
    private lateinit var circleImageView: CircleImageView
    private lateinit var fab: FloatingActionButton
    private val calendar = Calendar.getInstance()
    private lateinit var btnDatePicker: EditText


    private var db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalinfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = db.collection("customers")


        val firstName = binding.personalfirstnameEt
        val lastName = binding.personallastnameEt
        val dateOfBirth = binding.personaldobEt
        val mobileNumber = binding.personalmobilenumberEt

        userRef.whereEqualTo("user_id", userId).get().addOnSuccessListener{documents ->
            for (document in documents) {
                // Access data from the document
                val data = document.data
                val fname = data["first_name"]?.toString()
                val lname = data["last_name"]?.toString()
                val dob = data["date_of_birth"]?.toString()
                val mNo = data["mobile_number"]?.toString()

                firstName.setText(fname)
                lastName.setText(lname)
                dateOfBirth.setText(dob)
                mobileNumber.setText(mNo)
            }
        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Failed to load", Toast.LENGTH_SHORT).show()
        }

        circleImageView = binding.editPicture
        fab = binding.floatingButton

        binding.floatingButton.setOnClickListener{
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        btnDatePicker= view.findViewById(R.id.personaldobEt)
        btnDatePicker.keyListener = null
        btnDatePicker.setOnClickListener {
            // Show the DatePicker dialog
            showDatePicker()
        }

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.saveBtn.setOnClickListener{
            val profilequery = db.collection("customers").whereEqualTo("user_id", userId)
            val timeNow = Calendar.getInstance().time

            if(firstName.text.isNotEmpty() && lastName.text.isNotEmpty() && dateOfBirth.text.isNotEmpty() && mobileNumber.text.isNotEmpty()) {
                if (mobileNumber.text.trim().isDigitsOnly()) {
                    profilequery.get().addOnSuccessListener {
                        val updateMap = mapOf(
                            "first_name" to firstName.text.toString(),
                            "last_name" to lastName.text.toString(),
                            "date_of_birth" to dateOfBirth.text.toString(),
                            "mobile_number" to mobileNumber.text.toString().trim(),
                            "update_time" to timeNow
                        )
                        db.collection("customers").document(userId).update(updateMap)
                        Toast.makeText(requireActivity(), "Successfully edited", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireActivity(),"User data save failed",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(),"Mobile number must contain the digits only ",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(),"Fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        circleImageView.setImageURI(data?.data)
    }

    private fun showDatePicker() {
        val timeNow = Calendar.getInstance().time
        val datePickerDialog = DatePickerDialog(
            requireActivity(), {datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                datePicker.minDate = System.currentTimeMillis() - 1000
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

}