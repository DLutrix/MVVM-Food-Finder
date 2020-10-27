package com.dlutrix.foodfinder.repository.restaurantByCollection

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import com.dlutrix.foodfinder.data.source.restaurantByCollection.RestaurantByCollectionRemotePagingDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Singleton
class RestaurantByCollectionRepository @Inject constructor(
    private val api: ZomatoApiService
) {

    fun getRestaurantByCollection(
        collectionId: Int
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { RestaurantByCollectionRemotePagingDataSource(api, collectionId) }
    ).liveData
}