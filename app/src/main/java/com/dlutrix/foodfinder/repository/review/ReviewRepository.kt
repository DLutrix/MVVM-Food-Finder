package com.dlutrix.foodfinder.repository.review

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.dlutrix.foodfinder.data.model.Review
import com.dlutrix.foodfinder.data.source.ZomatoApiService
import com.dlutrix.foodfinder.data.source.review.ReviewRemoteDataSource
import retrofit2.Response
import javax.inject.Inject


/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) {

    suspend fun getReviews(resId: Int): Response<Review> =
        remoteDataSource.getReview(resId)
}