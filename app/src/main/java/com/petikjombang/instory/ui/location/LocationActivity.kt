package com.petikjombang.instory.ui.location

import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.petikjombang.instory.R
import com.petikjombang.instory.data.remote.response.ListStoryLocationItem
import com.petikjombang.instory.databinding.ActivityLocationBinding
import com.petikjombang.instory.ui.ViewModelFactory
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var locationBinding: ActivityLocationBinding
    private val locationViewModels: LocationViewModels by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationBinding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(locationBinding.root)

        supportActionBar?.title = "Location Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()

        locationViewModels.getLocationStory().observe(this){ result ->
            if (result != null) {
                when(result) {
                    is com.petikjombang.instory.data.Result.Loading -> {
                        true
                    }

                    is com.petikjombang.instory.data.Result.Success -> {
                        val data = result.data
                        addLocationStory(data.listStoryLocation)
                    }

                    is com.petikjombang.instory.data.Result.Error -> {
                        Toast.makeText(this, "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addLocationStory(listStoryLocation: ArrayList<ListStoryLocationItem>) {
        listStoryLocation?.forEach { location ->
            val latLng = LatLng(location.lat as Double, location.lon as Double)
            val addressName = getAddressName(location.lat, location.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(location.name).snippet(addressName))
            boundsBuilder.include(latLng)

            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    100
                )
            )
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String {
        var addressName: String? = null
        val geoCoder = Geocoder(this@LocationActivity, Locale.getDefault())
        try {
            val list = geoCoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d("Alamat: ", "getAddressName: $addressName")

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName.toString()

    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> true
        }
    }
}