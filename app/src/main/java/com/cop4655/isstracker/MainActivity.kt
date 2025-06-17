package com.cop4655.isstracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var btnLocation: Button
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvTime: TextView
    private lateinit var imageCrew: ImageButton
    private lateinit var locationText: TextView
    private lateinit var missionPatch: ImageButton
    private lateinit var progressBar: ProgressBar
    private var latLng: LatLng = LatLng(0.0, 0.0)
    private lateinit var issPlace: Place

    // Define a constant for the location permission request code
    private val LOCATION_PERMISSION_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize the location provider client
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        var issLatitude: Double = 0.0
        var issLongitude: Double = 0.0

        // Find the element view by its ID and
        // assign it to a variable.
        btnLocation = findViewById(R.id.btnLocation)
        tvLatitude = findViewById(R.id.tvLatitude)
        tvLongitude = findViewById(R.id.tvLongitude)
        tvTime = findViewById(R.id.tvTime)
        progressBar = findViewById(R.id.idLoadingPB)
        imageCrew = findViewById(R.id.imageCrew)
        locationText = findViewById(R.id.locationText)
        missionPatch = findViewById(R.id.missionPatch)
        Picasso.get()
            .load("https://upload.wikimedia.org/wikipedia/commons/3/3a/The_official_portrait_of_the_Expedition_73_crew_%28iss073-s-002%29.jpg")
            .placeholder(R.drawable.iss_stroke_159x100_purple)
            .resize(300, 300)
            .into(imageCrew)

        Picasso.get()
            .load("https://upload.wikimedia.org/wikipedia/commons/a/a4/ISS_Expedition_73_Patch.png")
            .placeholder(R.drawable.iss_stroke_159x100_purple)
            .resize(300, 300)
            .into(missionPatch)

        // Set an OnClickListener on the button view.
        btnLocation.setOnClickListener {
            ApiCall().getLocation(this) { coordinates ->
                issLatitude = coordinates.iss_position.latitude
                issLongitude = coordinates.iss_position.longitude
                addToMap(issLatitude, issLongitude)
            }
        }

        imageCrew.setOnClickListener {
            val intent = Intent(this, CrewActivity::class.java)
            startActivity(intent)
        }

        // show the progress bar
        progressBar.visibility = View.VISIBLE

        getCurrentLocation()

        ApiCall().getLocation(this) { coordinates ->
            issLatitude = coordinates.iss_position.latitude
            issLongitude = coordinates.iss_position.longitude
            addToMap(issLatitude, issLongitude)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addMarkers(googleMap: GoogleMap, places: List<Place>) {
        places.forEach { place ->
            googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(place.latLng)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iss_stroke_159x100_purple))
            )
        }
    }

    private fun addToMap(issLatitude: Double, issLongitude: Double) {
        // Call the getLocation() method of the ApiCall class,
        // passing a callback function as a parameter.
        ApiCall().getLocation(this) { coordinates ->
            // Set the text of the text view to the
            // joke value returned by the API response.
            var latValueDegrees = issLatitude.toString() + "\u00B0"
            var lngValueDegrees = issLongitude.toString() + "\u00B0"
            tvLatitude.text = latValueDegrees
            tvLongitude.text = lngValueDegrees
            latLng = LatLng(issLatitude, issLongitude)

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val timeDate = java.util.Date(coordinates.timestamp * 1000)
            tvTime.text = sdf.format(timeDate)

            issPlace = Place(
                name = "ISS",
                latLng = latLng,
                address = "ISS",
                rating = 1F
            )

            val places = List(1) {
                issPlace
            }

            // hide the progress bar
            progressBar.visibility = View.GONE

            val mapFragment = supportFragmentManager.findFragmentById(
                R.id.map_fragment
            ) as? SupportMapFragment
            mapFragment?.getMapAsync { googleMap ->
                addMarkers(googleMap, places)
            }

            mapFragment?.getMapAsync { googleMap ->
                // Ensure all places are visible in the map.
                googleMap.setOnMapLoadedCallback {
                    //val bounds = LatLngBounds.builder()
                    //places.forEach { bounds.include(it.latLng) }
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1F))
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(1f), 2000, null)
                }
            }
        }
    }


    // Function to get the current location
    private fun getCurrentLocation() {
        // Check if the location permission is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }

        // Fetch the last known location
        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // If location is available, extract latitude and longitude
                val lat = location.latitude
                val lon = location.longitude

                // Display location in the TextView
                locationText.text = "Latitude: $lat\nLongitude: $lon"
            } else {
                // If location is null, display an error message
                locationText.text = "Unable to get location"
            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the permission was granted
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is granted, fetch the location
            getCurrentLocation()
        } else {
            // If permission is denied, update the TextView with an error message
            locationText.text = "Location permission denied"
        }
    }
}

