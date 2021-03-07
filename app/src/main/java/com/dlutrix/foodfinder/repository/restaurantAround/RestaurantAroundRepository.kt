package com.dlutrix.foodfinder.repository.restaurantAround

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import com.dlutrix.foodfinder.data.source.restaurantAround.RestaurantAroundRemotePagingDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantAroundRepository @Inject constructor(
    private val api: ZomatoApiService
) {

    fun getAllRestaurant(
        lat: Double,
        long: Double
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { RestaurantAroundRemotePagingDataSource(api, lat, long) }
    ).flow
}