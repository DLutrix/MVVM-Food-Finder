package com.dlutrix.foodfinder.data.model


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("reviews_count")
    val reviewsCount: Int,
    @SerializedName("reviews_shown")
    val reviewsShown: Int,
    @SerializedName("reviews_start")
    val reviewsStart: Int,
    @SerializedName("user_reviews")
    val userReviews: List<UserReview>
)