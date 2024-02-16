package com.example.ayosapp.ayosPackage

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.ayosapp.R
import com.example.ayosapp.databinding.ActivityAyosMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import java.io.IOException
import java.util.Locale

class AyosMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: MarkerOptions
    private lateinit var binding: ActivityAyosMapBinding
    private val FINE_PERMISSION_CODE = 1

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var  currentLocation: Location
    private lateinit var geocoder: Geocoder
    private lateinit var selectedLocation: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLocation()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        binding = ActivityAyosMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Places.initialize(applicationContext, "AIzaSyClRXgmw_ht7v6_AJ7zZ6uvWzv87CVgYBA")


        val confirmButton: Button = findViewById(R.id.confirmBtn)
        confirmButton.setOnClickListener {
            val location = getAddress(selectedLocation).toString()
            val bundle = Bundle()
            bundle.putString("location", location)

            val fragmentB = addAddressFragment()
            fragmentB.arguments = bundle
            addFragmentOnTop(fragmentB)

            //val intent = Intent(this, AyosBookingActivity::class.java)
            //intent.putExtra("location", location)
            //startActivity(intent)

        }

        geocoder = Geocoder(this, Locale.getDefault())

    }
    private fun initializeLocation() {
        // Perform location initialization here
        currentLocation = Location("dummy")
        currentLocation.latitude = 0.0
        currentLocation.longitude = 0.0
        marker = MarkerOptions()
            .position(LatLng(0.0, 0.0)) // Default position
            .title("Dummy Marker") // Default title
            .snippet("This is a dummy marker")
    }
    private fun getLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {location ->
                currentLocation.latitude = location.latitude
                currentLocation.longitude = location.longitude
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in current location and move the camera
        val curLoc = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.addMarker(MarkerOptions().position(curLoc).title("Current location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLoc))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMapClickListener { latLng ->
            // Clear previous markers
            mMap.clear()
            // Add new marker

            mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        }

        mMap.setOnCameraMoveListener {
            // Update marker position to stay in the middle of the screen
            val target = mMap.cameraPosition.target
            marker.position(target)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }else{
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getAddress(latLng: LatLng) {
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses!![0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addFragmentOnTop(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.map, fragment)
        fragmentTransaction.addToBackStack(null) // This line allows you to go back to the previous fragment when pressing the back button
        fragmentTransaction.commit()
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}