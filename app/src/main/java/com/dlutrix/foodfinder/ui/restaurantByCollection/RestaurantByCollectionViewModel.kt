package com.dlutrix.foodfinder.ui.restaurantByCollection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dlutrix.foodfinder.repository.restaurantByCollection.RestaurantByCollectionRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantByCollectionViewModel @AssistedInject constructor(
    private val restaurantByCollectionRepository: RestaurantByCollectionRepository,
    @Assisted private val collectionId: Int
) : ViewModel() {

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(collectionId: Int): RestaurantByCollectionViewModel
    }

    val restaurantByCollection =
        restaurantByCollectionRepository.getRestaurantByCollection(collectionId)
            .cachedIn(viewModelScope)


    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: AssistedFactory,
            collectionId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(collectionId) as T
            }
        }
    }
}