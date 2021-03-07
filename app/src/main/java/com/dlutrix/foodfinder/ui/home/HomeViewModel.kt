package com.dlutrix.foodfinder.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dlutrix.foodfinder.repository.restaurantAround.RestaurantAroundRepository
import com.dlutrix.foodfinder.repository.restaurantCollection.RestaurantCollectionRepository
import com.dlutrix.foodfinder.utils.Constant
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LAT
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LONG
import com.dlutrix.foodfinder.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


/**
 * w0rm1995 on 22/10/20.
 * risfandi@dlutrix.com
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantCollectionRepository: RestaurantCollectionRepository,
    private val restaurantAroundRepository: RestaurantAroundRepository,
    var sharedPreferences: SharedPreferences,
    locationHelper: LocationHelper,
) : ViewModel() {

    private val lat = sharedPreferences.getString(Constant.KEY_LAT, DEFAULT_LAT) ?: DEFAULT_LAT
    private val long = sharedPreferences.getString(Constant.KEY_LONG, DEFAULT_LONG) ?: DEFAULT_LONG

    val locationLiveData = locationHelper.getUserLocation().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val location = Pair(lat.toDouble(), long.toDouble())

    private val _userLocation = MutableLiveData(location)

    val restaurantCollection = _userLocation.switchMap {
        restaurantCollectionRepository.getRestaurantCollectionFlow(it.first, it.second)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    private val _restaurantAround: MutableLiveData<Pair<Double, Double>> = MutableLiveData(location)

    val restaurantAround = _restaurantAround.switchMap {
        restaurantAroundRepository.getAllRestaurant(it.first, it.second)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
            .cachedIn(viewModelScope)
    }

    val locationa = MutableLiveData(location)

    val locationName = locationa.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(locationHelper.getLocationName(it.first, it.second))
        }
    }

    fun getRemoteData(lat: Double, long: Double) {
        val location = Pair(lat, long)
        _userLocation.value = location
        locationa.value = location
        _restaurantAround.value = location
    }
}