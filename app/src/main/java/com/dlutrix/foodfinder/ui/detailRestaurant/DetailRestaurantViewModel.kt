package com.dlutrix.foodfinder.ui.detailRestaurant

import androidx.lifecycle.*
import com.dlutrix.foodfinder.repository.review.ReviewRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers


/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class DetailRestaurantViewModel @AssistedInject constructor(
    private val reviewRepository: ReviewRepository,
    @Assisted private val resId: Int
) : ViewModel() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(resId: Int): DetailRestaurantViewModel
    }

    private val _reviews: MutableLiveData<Int> = MutableLiveData(resId)

    val reviewsLiveData = _reviews.switchMap {
        reviewRepository.getReviews(resId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
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