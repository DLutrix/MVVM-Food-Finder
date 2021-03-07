package com.dlutrix.foodfinder.data.source.restaurantCollection

import com.dlutrix.foodfinder.data.source.ZomatoApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantCollectionRemoteDataSource @Inject constructor(
    private val api: ZomatoApiService
) {

    fun getRestaurantCollectionFlow(lat: Double, long: Double) = flow {
        emit(api.getRestaurantCollection(lat, long))
    }
}
