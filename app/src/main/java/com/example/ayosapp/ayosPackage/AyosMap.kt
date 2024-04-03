package com.example.ayosapp.ayosPackage

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.ayosapp.R
import com.example.ayosapp.databinding.ActivityAyosMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    private lateinit var locationManager: LocationManager
    var currentMarker:Marker? = null
    private val confirmButton: Button? = null
    private val REQUEST_CODE =101
    private var addressid:String? = null
    private var instructions:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLocation()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
        binding = ActivityAyosMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Places.initialize(applicationContext, "AIzaSyClRXgmw_ht7v6_AJ7zZ6uvWzv87CVgYBA")

        val confirmButton: Button = findViewById(R.id.confirmBtnMap)
        confirmButton.setOnClickListener {
            val location = getAddress(currentLocation.latitude, currentLocation.longitude)
            val bundle = Bundle()
            bundle.putString("location", location.toString())
            bundle.putDouble("latitude", currentLocation.latitude)
            bundle.putDouble("longitude", currentLocation.longitude)
            bundle.putString("addressid", addressid)
            bundle.putString("instructions",instructions)

            val mFragmentManager = supportFragmentManager
            val mFragmentTransaction = mFragmentManager.beginTransaction()
            val fragmentB = addAddressFragment()
            fragmentB.arguments = bundle
            mFragmentTransaction.add(R.id.map, fragmentB).commit()
//            replaceFragment(fragmentB)

            //updateButtonVisibility()
        }

        geocoder = Geocoder(this, Locale.getDefault())

    }


    private fun initializeLocation() {
        currentLocation = Location("dummy")
        instructions = intent.getStringExtra("instructions")
        addressid = intent.getStringExtra("addressid")
        currentLocation.latitude = intent.getDoubleExtra("latitude",0.0)
        currentLocation.longitude = intent.getDoubleExtra("longitude",0.0)

        marker = MarkerOptions()
            .position(LatLng(currentLocation.latitude, currentLocation.longitude))

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
        currentLocation.latitude = intent.getDoubleExtra("latitude",0.0)
        currentLocation.longitude = intent.getDoubleExtra("longitude",0.0)
        val curLoc = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.addMarker(MarkerOptions().position(curLoc))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude),16.0f))
        //drawMarker(curLoc)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMapClickListener { latLng ->
            Log.d("DEBUG", "old location: {${currentLocation.latitude} ${currentLocation.longitude}}")
            // Clear previous marker
            mMap.clear()
            // Add new marker
            mMap.addMarker(MarkerOptions().position(latLng))
            currentLocation.longitude=latLng.longitude
            currentLocation.latitude=latLng.latitude
            Log.d("DEBUG", "new location: {${currentLocation.latitude} ${currentLocation.longitude}}")
            moveToCurrentLocation(LatLng(currentLocation.latitude,currentLocation.longitude))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(curLoc))
            val zoomLevel = 16.0f
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), zoomLevel)
            mMap.moveCamera(cameraUpdate)
        }
    }
    private fun moveToCurrentLocation(currentLocation: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
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

    private fun getAddress(lat: Double, lon: Double): String?{
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, lon,1)
        return address?.get(0)?.getAddressLine(0).toString()
    }

    private fun drawMarker(latlong: LatLng){
        MarkerOptions().position(latlong)
            .snippet(getAddress(latlong.latitude, latlong.longitude)).draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
        currentMarker=mMap.addMarker(marker)
        currentMarker?.showInfoWindow()
        currentLocation.latitude = latlong.latitude
        currentLocation.longitude = latlong.longitude
    }
    fun hideButton() {
        confirmButton?.visibility = View.GONE
    }
    fun showButton() {
        confirmButton?.visibility = View.VISIBLE
    }
    /*
    fun getCurrentFragment(): Fragment? {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible) {
                return fragment
            }
        }
        return null
    }

    // Function to update button visibility based on the active fragment
    fun updateButtonVisibility() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is MapFragment) {
            showButton()
        } else {
            hideButton()
        }
    }*/

    companion object {
        private const val REQUEST_CODE = 100
    }
}