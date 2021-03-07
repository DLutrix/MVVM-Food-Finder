package com.dlutrix.foodfinder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LAT
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LONG
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
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
    private val geocoder = Geocoder(context, Locale.getDefault())

    fun getUserLocation() = flow {
        try {
            val location = fusedLocationClient.awaitLastLocation()
            emit(location)
        } catch (_: Exception) {
            emit(Pair(DEFAULT_LAT, DEFAULT_LONG))
        }
    }


    fun getLocationName(lat: Double, long: Double): String {
        return try {
            val location = geocoder.getFromLocation(lat, long, 1)
            location[0].thoroughfare
        } catch (_: Exception) {
            "Food finder"
        }
    }
}