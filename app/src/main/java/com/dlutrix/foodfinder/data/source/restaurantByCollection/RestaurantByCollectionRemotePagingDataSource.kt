package com.dlutrix.foodfinder.data.source.restaurantByCollection

import androidx.paging.PagingSource
import com.dlutrix.foodfinder.data.model.Restaurant
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import com.dlutrix.foodfinder.utils.Constant.STARTING_PAGE_COUNT
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantByCollectionRemotePagingDataSource (
    private val api: ZomatoApiService,
    private val collectionId: Int,
) : PagingSource<Int, Restaurant>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Restaurant> {
        val position = params.key ?: STARTING_PAGE_COUNT

        return try {
            val response = api.getRestaurantByCollection(collectionId, 20, position)
            val itemData = response.restaurants

            LoadResult.Page(
                data = itemData,
                prevKey = if (position == STARTING_PAGE_COUNT) null else position - 20,
                nextKey = if (position == 100) null else position + 20
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}