package com.dlutrix.foodfinder.data.source

import com.dlutrix.foodfinder.data.model.RestaurantCollection
import com.dlutrix.foodfinder.data.model.Review
import com.dlutrix.foodfinder.data.model.SearchRestaurant
import com.dlutrix.foodfinder.utils.Constant.HEADER_1
import com.dlutrix.foodfinder.utils.Constant.HEADER_2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
interface ZomatoApiService {

    @Headers(
        HEADER_1,
        HEADER_2
    )
    @GET("collections")
    suspend fun getRestaurantCollection(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("count") count: Int = 7,
    ): Response<RestaurantCollection>


    @Headers(
        HEADER_1,
        HEADER_2
    )
    @GET("search")
    suspend fun getRestaurantAround(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("count") count: Int = 15,
        @Query("start") start: Int = 0,
        @Query("radius") radius: Double = 2000.0
    ): SearchRestaurant


    @Headers(
        HEADER_1,
        HEADER_2
    )
    @GET("search")
    suspend fun getRestaurantByCollection(
        @Query("collection_id") collectionId: Int,
        @Query("count") count: Int,
        @Query("start") start: Int,
    ): SearchRestaurant


    @Headers(
        HEADER_1,
        HEADER_2
    )
    @GET("reviews")
    suspend fun getReviews(
        @Query("res_id") resId: Int,
        @Query("count") count: Int = 10,
        @Query("start") start: Int = 0,
    ): Response<Review>
}