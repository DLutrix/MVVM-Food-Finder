package com.dlutrix.foodfinder.data.source.review

import com.dlutrix.foodfinder.data.source.ZomatoApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * w0rm1995 on 17/10/20.
 * risfandi@dlutrix.com
 */
class ReviewRemoteDataSource @Inject constructor(
    private val api: ZomatoApiService
) {

    fun getReviews(resId: Int) = flow {
        emit(api.getReviews(resId))
    }
}