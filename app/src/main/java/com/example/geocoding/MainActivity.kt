package com.example.geocoding

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.IOException

private const val TAG = "GEOCODE_PLACE_ACTIVITY"

class MainActivity : AppCompatActivity() {

    private lateinit var placeNameInput: EditText
    private lateinit var mapButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeNameInput = findViewById(R.id.place_name_input)
        mapButton = findViewById(R.id.map_button)

        mapButton.setOnClickListener {
            val placeName = placeNameInput.text.toString()
            if (placeName.isBlank()) {
                Toast.makeText( this, getString(R.string.no_place_entered_error), Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "About to geocode $placeName")
                showMapForPlace(placeName)
            }
        }
    }
    private fun showMapForPlace(placeName: String) {
        val geocoder = Geocoder(this)

        try {
            val addresses = geocoder.getFromLocationName(placeName, 1)

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses.first()
                    Log.d(TAG, "First address is $address")
                    val geoUriString = "geo:${address.latitude},${address.longitude}"
                    Log.d(TAG, "Using geo uri $geoUriString")
                    val geoUri = Uri.parse(geoUriString)
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    Log.d(TAG, "Launching map activity")
                    startActivity(mapIntent)
                } else {
                    Log.d(TAG, "No places found for string $placeName")
                    Toast.makeText(this, getString(R.string.no_places_found_message), Toast.LENGTH_SHORT).show()
                }
            }
        } catch ( e: IOException) {
            Log.e(TAG, "Unable to gecode place $placeName", e)
            Toast.makeText(this, "Sorry unable to geocode place. Are you online?", Toast.LENGTH_LONG).show()
        }
    }
}