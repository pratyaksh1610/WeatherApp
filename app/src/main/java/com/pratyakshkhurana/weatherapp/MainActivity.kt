package com.pratyakshkhurana.weatherapp

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pratyakshkhurana.weatherapp.Adapters.OnSearchViewHistoryItemClicked
import com.pratyakshkhurana.weatherapp.Adapters.SearchViewHistoryAdapter
import com.pratyakshkhurana.weatherapp.SharedPreferences.SharedPrefs
import com.pratyakshkhurana.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnSearchViewHistoryItemClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchText: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var listOfSearchViewHistoryItems: MutableList<String> =
        mutableListOf(
            "Adoni",
            "Amaravati",
            "Anantapur",
            "Srikakulam",
            "Tirupati",
            "Vijayawada",
            "Visakhapatnam",
            "Vizianagaram",
            "Yemmiganur",
            "Adoni",
            "Amaravati",
            "Anantapur",
            "Srikakulam",
            "Tirupati",
            "Vijayawada",
            "Visakhapatnam",
            "Vizianagaram",
            "Yemmiganur",
            "Adoni",
            "Amaravati",
            "Anantapur",
            "Srikakulam",
            "Tirupati",
            "Vijayawada",
            "Visakhapatnam",
            "Vizianagaram",
            "Yemmiganur",
        )

    //    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val requestCode = 101
    private lateinit var sharedPreferences: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // request location permission and get current location lat/long coordinates
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()

        binding.apply {
            searchView.editText.setOnEditorActionListener(
                object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        p0: TextView?,
                        p1: Int,
                        p2: KeyEvent?,
                    ): Boolean {
                        val text = p0?.text.toString()
                        searchText = text
                        // automatically comes back to search bar when search icon clicked on keyboard
                        binding.searchView.handleBackInvoked()

                        Toast.makeText(applicationContext, searchText, Toast.LENGTH_SHORT).show()
                        binding.searchBar.setText(searchText)
                        return true
                    }
                },
            )
        }
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
//                performAction(...)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        val lat = location?.latitude
                        val lon = location?.longitude
                        Toast.makeText(this, "$lat $lon", Toast.LENGTH_SHORT).show()
                        // Got last known location. In some rare situations this can be null.
                    }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
//                showInContextUI(...)
                showAskLocationPermissionDialog()
            }

            else -> {
                // You can directly ask for the permission.
                // if user denies , then head over to system settings in onRequestPermissionsResult()
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    requestCode,
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            this.requestCode -> {
                // If request is cancelled, the result arrays are empty.
                if ((
                        grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED
                    )
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    showAskLocationPermissionDialog()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                showAskLocationPermissionDialog()
                // Ignore all other requests.
            }
        }
    }

    private fun showAskLocationPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Ask location permission")
            .setIcon(R.drawable.location_on_24px)
            .setMessage("Allow us to access this device's location !")
            .setPositiveButton("Ok") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun initialiseSearchViewRecyclerView() {
        binding.recyclerViewSearchItems.layoutManager =
            LinearLayoutManager(this)
        val adapter = SearchViewHistoryAdapter(listOfSearchViewHistoryItems, this)
        binding.recyclerViewSearchItems.adapter = adapter
    }

    override fun onSearchViewHistoryItemClickedResponse(pos: Int) {
        Toast.makeText(this, "item clicked search view ki", Toast.LENGTH_SHORT).show()
    }
}
