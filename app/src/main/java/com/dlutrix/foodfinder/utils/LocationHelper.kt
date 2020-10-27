package com.dlutrix.foodfinder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Singleton
class LocationHelper @Inject constructor(
    @ApplicationContext context: Context
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private var location: MutableLiveData<Location> = MutableLiveData()
    private var locationName: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("MissingPermission")
    fun getLocation(): LiveData<Location> {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                loc?.let {
                    location.value = loc
                }
            }

        return location
    }

    private val geocoder = Geocoder(context, Locale.getDefault())

    fun getLocationName(lat: Double, long: Double): LiveData<String> {


        try {
            val location = geocoder.getFromLocation(lat, long, 1)
            locationName.value = location[0].thoroughfare
        } catch (_: Exception) {
            locationName.value = "Food Finder"
        }

        return locationName
    }

}