package com.dlutrix.foodfinder.repository.restaurantCollection

import com.dlutrix.foodfinder.data.model.RestaurantCollection
import com.dlutrix.foodfinder.data.source.restaurantCollection.RestaurantCollectionRemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantCollectionRepository @Inject constructor(
    private val remoteDataSource: RestaurantCollectionRemoteDataSource
) {
    suspend fun getRestaurantCollection(
        lat: Double,
        long: Double
    ): Response<RestaurantCollection> = remoteDataSource.getRestaurantCollection(lat, long)
}