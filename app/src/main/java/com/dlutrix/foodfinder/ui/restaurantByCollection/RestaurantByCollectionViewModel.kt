package com.dlutrix.foodfinder.ui.restaurantByCollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dlutrix.foodfinder.repository.restaurantByCollection.RestaurantByCollectionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
class RestaurantByCollectionViewModel @AssistedInject constructor(
    restaurantByCollectionRepository: RestaurantByCollectionRepository,
    @Assisted private val collectionId: Int
) : ViewModel() {

    @dagger.assisted.AssistedFactory
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