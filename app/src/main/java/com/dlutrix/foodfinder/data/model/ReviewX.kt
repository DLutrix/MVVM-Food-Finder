package com.dlutrix.foodfinder.data.model


import com.google.gson.annotations.SerializedName

data class ReviewX(
    @SerializedName("comments_count")
    val commentsCount: Int,
    val id: Int,
    val likes: Int,
    val rating: Float,
    @SerializedName("rating_color")
    val ratingColor: String,
    @SerializedName("rating_text")
    val ratingText: String,
    @SerializedName("review_text")
    val reviewText: String,
    @SerializedName("review_time_friendly")
    val reviewTimeFriendly: String,
    val timestamp: Int,
    val user: User
)