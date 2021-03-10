package com.dlutrix.foodfinder.repository.restaurantCollection

import com.dlutrix.foodfinder.data.source.restaurantCollection.RestaurantCollectionRemoteDataSource
import com.dlutrix.foodfinder.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantCollectionRepository @Inject constructor(
    private val remoteDataSource: RestaurantCollectionRemoteDataSource
) {

    fun getRestaurantCollectionFlow(
        lat: Double,
        long: Double
    ) = flow {
        remoteDataSource.getRestaurantCollectionFlow(lat, long).onStart {
            emit(Resource.loading(null))
        }.catch {
            emit(
                Resource.error(
                    null,
                    "Failed to retrieve data from server please check your network and try again",
                    true
                )
            )
        }.collect {
            if (it.isSuccessful) {
                emit(Resource.success(it.body()!!))
            } else {
                emit(Resource.error(null, "Your location is not available", false))
            }
        }
    }
}