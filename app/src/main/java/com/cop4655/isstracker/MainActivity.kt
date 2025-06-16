package com.cop4655.isstracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
import com.squareup.picasso.Picasso;

class MainActivity : AppCompatActivity() {
    private lateinit var btnLocation: Button
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvTime: TextView
    private lateinit var imageCrew: ImageButton
    private lateinit var missionPatch: ImageButton
    private lateinit var progressBar: ProgressBar
    private var latLng: LatLng = LatLng(0.0, 0.0)
    private lateinit var issPlace: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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
        missionPatch = findViewById(R.id.missionPatch)
        Picasso.get()
            .load("https://upload.wikimedia.org/wikipedia/commons/3/3a/The_official_portrait_of_the_Expedition_73_crew_%28iss073-s-002%29.jpg")
            .placeholder(R.drawable.iss_stroke_159x100_purple)
            .resize(300,300)
            .into(imageCrew)

        Picasso.get()
            .load("https://upload.wikimedia.org/wikipedia/commons/a/a4/ISS_Expedition_73_Patch.png")
            .placeholder(R.drawable.iss_stroke_159x100_purple)
            .resize(300,300)
            .into(missionPatch)

        // Set an OnClickListener on the button view.
        btnLocation.setOnClickListener {
            ApiCall().getLocation(this) { coordinates ->
                issLatitude = coordinates.issPosition.latitude
                issLongitude = coordinates.issPosition.longitude
                addToMap(issLatitude, issLongitude)
            }
        }

        imageCrew.setOnClickListener {
            val intent = Intent(this, CrewActivity::class.java)
            startActivity(intent)
        }

        // show the progress bar
        progressBar.visibility = View.VISIBLE

        ApiCall().getLocation(this) { coordinates ->
            issLatitude = coordinates.issPosition.latitude
            issLongitude = coordinates.issPosition.longitude
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

