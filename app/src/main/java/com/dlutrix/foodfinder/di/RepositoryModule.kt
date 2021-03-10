package com.dlutrix.foodfinder.di

import com.dlutrix.foodfinder.data.source.ZomatoApiService
import com.dlutrix.foodfinder.data.source.restaurantCollection.RestaurantCollectionRemoteDataSource
import com.dlutrix.foodfinder.data.source.review.ReviewRemoteDataSource
import com.dlutrix.foodfinder.repository.restaurantAround.RestaurantAroundRepository
import com.dlutrix.foodfinder.repository.restaurantByCollection.RestaurantByCollectionRepository
import com.dlutrix.foodfinder.repository.restaurantCollection.RestaurantCollectionRepository
import com.dlutrix.foodfinder.repository.review.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideReviewRepository(
        reviewRemoteDataSource: ReviewRemoteDataSource
    ): ReviewRepository = ReviewRepository(reviewRemoteDataSource)

    @Provides
    @ViewModelScoped
    fun provideRestaurantColletionRepository(
        restaurantCollectionRemoteDataSource: RestaurantCollectionRemoteDataSource
    ): RestaurantCollectionRepository =
        RestaurantCollectionRepository(restaurantCollectionRemoteDataSource)

    @Provides
    @ViewModelScoped
    fun provideRestaurantByCollectionRepository(
        zomatoApiService: ZomatoApiService
    ): RestaurantByCollectionRepository = RestaurantByCollectionRepository(zomatoApiService)

    @Provides
    @ViewModelScoped
    fun provideRestaurantAroundRepository(
        zomatoApiService: ZomatoApiService
    ): RestaurantAroundRepository = RestaurantAroundRepository(zomatoApiService)
}