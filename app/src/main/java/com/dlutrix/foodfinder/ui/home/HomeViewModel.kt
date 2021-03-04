package com.dlutrix.foodfinder.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dlutrix.foodfinder.data.model.RestaurantCollection
import com.dlutrix.foodfinder.repository.restaurantAround.RestaurantAroundRepository
import com.dlutrix.foodfinder.repository.restaurantCollection.RestaurantCollectionRepository
import com.dlutrix.foodfinder.utils.Constant
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LAT
import com.dlutrix.foodfinder.utils.Constant.DEFAULT_LONG
import com.dlutrix.foodfinder.utils.LocationHelper
import com.dlutrix.foodfinder.utils.NetworkHelper
import com.dlutrix.foodfinder.utils.Resource
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
    networkHelper: NetworkHelper,
    locationHelper: LocationHelper,
) : ViewModel() {

    private val lat = sharedPreferences.getString(Constant.KEY_LAT, DEFAULT_LAT) ?: DEFAULT_LAT
    private val long = sharedPreferences.getString(Constant.KEY_LONG, DEFAULT_LONG) ?: DEFAULT_LONG

    val locationLiveData = locationHelper.getLocation()

    private val location = Pair(lat.toDouble(), long.toDouble())

    private val _restaurantCollection: MutableLiveData<Pair<Double, Double>> =
        MutableLiveData(location)

    private val _restaurantAround: MutableLiveData<Pair<Double, Double>> = MutableLiveData(location)

    private val _locationName: MutableLiveData<Pair<Double, Double>> = MutableLiveData(location)

    val restaurantCollection: LiveData<Resource<RestaurantCollection>> =
        _restaurantCollection.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(Resource.loading(null))
                try {
                    if (networkHelper.isNetworkConnected()) {
                        val response = restaurantCollectionRepository
                            .getRestaurantCollection(it.first, it.second)
                        if (response.code() == 200) {
                            emit(Resource.success(response.body()!!))
                        } else {
                            emit(
                                Resource.error(
                                    data = null,
                                    "Your location is not available",
                                    false
                                )
                            )
                        }
                    } else {
                        emit(
                            Resource.error(
                                data = null,
                                "Failed to retrieve data from server please check your network and try again",
                                true
                            )
                        )
                    }
                } catch (exception: Exception) {
                    emit(Resource.error(data = null, "Unexpected Error!", true))
                }
            }
        }

    val restaurantAround = _restaurantAround.switchMap {
        restaurantAroundRepository.getAllRestaurant(it.first, it.second).cachedIn(viewModelScope)
    }

    val locationName = _locationName.switchMap {
        locationHelper.getLocationName(it.first, it.second)
    }

    fun getRemoteData(lat: Double, long: Double) {
        val location = Pair(lat, long)
        _restaurantCollection.value = location
        _restaurantAround.value = location
        _locationName.value = location
    }
}