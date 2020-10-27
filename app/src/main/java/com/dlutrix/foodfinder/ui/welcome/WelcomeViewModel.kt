package com.dlutrix.foodfinder.ui.welcome

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dlutrix.foodfinder.utils.LocationHelper

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class WelcomeViewModel @ViewModelInject constructor(
    locationHelper: LocationHelper,
    var sharedPreferences: SharedPreferences
    ) : ViewModel() {

    val location = locationHelper.getLocation()

}