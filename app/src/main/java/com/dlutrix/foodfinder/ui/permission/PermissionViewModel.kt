package com.dlutrix.foodfinder.ui.permission

import androidx.lifecycle.*
import com.dlutrix.foodfinder.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@HiltViewModel
class PermissionViewModel @Inject constructor(
    locationHelper: LocationHelper,
) : ViewModel() {

    val location =
        locationHelper.getUserLocation()
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val _locationPermissionGranted = MutableLiveData(false)
    val locationPermissionGranted: LiveData<Boolean> get() = _locationPermissionGranted

    fun setLocationPermission(isGranted: Boolean) {
        _locationPermissionGranted.value = isGranted
    }

}