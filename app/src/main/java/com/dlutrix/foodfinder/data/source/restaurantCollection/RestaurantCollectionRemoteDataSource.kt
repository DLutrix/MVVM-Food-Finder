package com.dlutrix.foodfinder.data.source.restaurantCollection

import com.dlutrix.foodfinder.data.model.RestaurantCollection
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantCollectionRemoteDataSource @Inject constructor(
    private val api: ZomatoApiService
) {
    suspend fun getRestaurantCollection(lat: Double, long: Double): Response<RestaurantCollection> =
        api.getRestaurantCollection(lat, long)
}
