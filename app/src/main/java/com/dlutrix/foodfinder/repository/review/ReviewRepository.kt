package com.dlutrix.foodfinder.repository.review

import com.dlutrix.foodfinder.data.source.review.ReviewRemoteDataSource
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
class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) {

    fun getReviews(resId: Int) = flow {
        remoteDataSource.getReviews(resId).onStart {
            emit(Resource.loading(null))
        }.catch {
            emit(
                Resource.error(
                    data = null,
                    "Failed to retrieve data from server please check your network and try again",
                    true
                )
            )
        }.collect {
            if (it.isSuccessful) {
                if (it.body()?.userReviews!!.isEmpty()) {
                    emit(Resource.error(data = null, "No review yet", false))
                } else {
                    emit(Resource.success(it.body()!!))
                }
            } else {
                emit(
                    Resource.error(
                        data = null,
                        "Failed to retrieve data from server",
                        false
                    )
                )
            }
        }
    }
}