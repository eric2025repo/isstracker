package com.cop4655.isstracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var btnLocation: Button
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvTime: TextView
    private lateinit var progressBar: ProgressBar
    private var latLng: LatLng = LatLng(0.0, 0.0)
    private lateinit var issPlace: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var issLatitude: Double = 0.0
        var issLongitude: Double = 0.0

        // Find the button view by its ID and
        // assign it to a variable.
        btnLocation = findViewById(R.id.btnLocation)

        // Find the text view by its ID and
        // assign it to a variable.
        tvLatitude = findViewById(R.id.tvLatitude)

        // Find the text view by its ID and
        // assign it to a variable.
        tvLongitude = findViewById(R.id.tvLongitude)

        // Find the text view by its ID and
        // assign it to a variable.
        tvTime = findViewById(R.id.tvTime)

        // Find the progress bar and assign
        // it to the variable.
        progressBar = findViewById(R.id.idLoadingPB)

        // Set an OnClickListener on the button view.
        btnLocation.setOnClickListener {
            ApiCall().getLocation(this) { coordinates ->
                issLatitude = coordinates.iss_position.latitude
                issLongitude = coordinates.iss_position.longitude
                addToMap(issLatitude, issLongitude)
            }
        }
        // show the progress bar

        progressBar.visibility = View.VISIBLE

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
            val marker = googleMap.addMarker(
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
}

