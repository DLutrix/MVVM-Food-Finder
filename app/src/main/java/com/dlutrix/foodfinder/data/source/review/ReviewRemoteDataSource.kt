package com.dlutrix.foodfinder.data.source.review

import com.dlutrix.foodfinder.data.model.Review
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import retrofit2.Response
import javax.inject.Inject


/**
 * w0rm1995 on 17/10/20.
 * risfandi@dlutrix.com
 */
class ReviewRemoteDataSource @Inject constructor(
    private val api: ZomatoApiService
) {

    suspend fun getReview(resId: Int): Response<Review> =
        api.getReviews(resId)
}