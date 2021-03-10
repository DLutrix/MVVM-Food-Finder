package com.dlutrix.foodfinder.ui.welcome

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dlutrix.foodfinder.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    locationHelper: LocationHelper,
    var sharedPreferences: SharedPreferences
) : ViewModel() {

    val location =
        locationHelper.getUserLocation().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

}