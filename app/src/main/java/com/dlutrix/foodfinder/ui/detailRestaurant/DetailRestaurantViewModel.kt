package com.dlutrix.foodfinder.ui.detailRestaurant

import android.util.Log
import androidx.lifecycle.*
import com.dlutrix.foodfinder.data.model.Review
import com.dlutrix.foodfinder.repository.review.ReviewRepository
import com.dlutrix.foodfinder.utils.NetworkHelper
import com.dlutrix.foodfinder.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers


/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class DetailRestaurantViewModel @AssistedInject constructor(
    private val reviewRepository: ReviewRepository,
    networkHelper: NetworkHelper,
    @Assisted private val resId: Int
) : ViewModel() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(resId: Int): DetailRestaurantViewModel
    }

    private val _reviews: MutableLiveData<Int> = MutableLiveData(resId)

    val reviews: LiveData<Resource<Review>> = _reviews.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {
                    val response = reviewRepository.getReviews(resId)
                    if (response.code() == 200) {
                        if (response.body()?.userReviews!!.isEmpty()) {
                            emit(Resource.error(data = null, "No review yet", false))
                        }
                        emit(Resource.success(response.body()!!))
                    } else {
                        emit(
                            Resource.error(
                                data = null,
                                "Failed to retrieve data from server",
                                false
                            )
                        )
                    }
                } else {
                    emit(
                        Resource.error(
                            data = null,
                            "Failed to retrieve data from server please check your network and try again",
                            true
                        )
                    )
                }
            } catch (exception: Exception) {
                Log.e("a", exception.message!!)
                emit(Resource.error(data = null, "Unexpected Error!", true))
            }
        }
    }

    fun getReviews() {
        _reviews.value = resId
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: AssistedFactory,
            resId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(resId) as T
            }
        }
    }
}