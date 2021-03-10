package com.dlutrix.foodfinder.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dlutrix.foodfinder.data.model.RestaurantX
import com.dlutrix.foodfinder.repository.restaurantAround.RestaurantAroundRepository
import com.dlutrix.foodfinder.repository.restaurantCollection.RestaurantCollectionRepository
import com.dlutrix.foodfinder.utils.Constant
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LAT
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LONG
import com.dlutrix.foodfinder.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * w0rm1995 on 22/10/20.
 * risfandi@dlutrix.com
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantCollectionRepository: RestaurantCollectionRepository,
    private val restaurantAroundRepository: RestaurantAroundRepository,
    private var sharedPreferences: SharedPreferences,
    private val locationHelper: LocationHelper,
) : ViewModel() {

    private val lat = sharedPreferences.getString(Constant.KEY_LAT, DEFAULT_LAT) ?: DEFAULT_LAT
    private val long = sharedPreferences.getString(Constant.KEY_LONG, DEFAULT_LONG) ?: DEFAULT_LONG

    private val _isError = MutableLiveData(true)
    val isError get() = _isError

    private val homeEventChannel = Channel<HomeEvent>()
    val homeEvent = homeEventChannel.receiveAsFlow()

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

    val locationName = _userLocation.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(locationHelper.getLocationName(it.first, it.second))
        }
    }

    private fun getRemoteData(lat: Double, long: Double) {
        val location = Pair(lat, long)
        _userLocation.value = location
        _restaurantAround.value = location
    }

    fun setIsError(isLoading: Boolean) {
        _isError.value = isLoading
    }

    fun refreshLocation() {
        viewModelScope.launch {
            locationHelper.getUserLocation().collect {
                homeEventChannel.send(HomeEvent.RefreshLocation(Pair(it.first, it.second)))
            }
        }
    }

    fun retryObserverIfLocationNotAvailable() {
        sharedPreferences.edit()
            .putString(Constant.KEY_LAT, DEFAULT_LAT)
            .putString(Constant.KEY_LONG, DEFAULT_LONG)
            .apply()

        getRemoteData(DEFAULT_LAT.toDouble(), DEFAULT_LONG.toDouble())
    }

    fun retryObserver() {
        getRemoteData(location.first, location.second)
    }

    fun retryObserverWhenLocationChanged(lat: Double, long: Double) {
        sharedPreferences.edit()
            .putString(Constant.KEY_LAT, lat.toString())
            .putString(Constant.KEY_LONG, long.toString())
            .apply()

        getRemoteData(lat, long)
    }

    fun onRestaurantItemClick(restaurantX: RestaurantX) = viewModelScope.launch {
        homeEventChannel.send(HomeEvent.NavigateToDetailRestaurant(restaurantX))
    }

    fun onRestaurantCollectionClick(collectionId: Int, collectionTitle: String) =
        viewModelScope.launch {
            homeEventChannel.send(
                HomeEvent.NavigateToRestaurantCollection(
                    collectionId,
                    collectionTitle
                )
            )
        }

    sealed class HomeEvent {
        data class RefreshLocation(val location: Pair<String, String>) : HomeEvent()
        data class NavigateToDetailRestaurant(val restaurantX: RestaurantX) : HomeEvent()
        data class NavigateToRestaurantCollection(
            val collectionId: Int,
            val collectionTitle: String
        ) : HomeEvent()
    }
}