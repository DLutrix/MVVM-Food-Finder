package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Parcelize
data class UserRating(
    @SerializedName("aggregate_rating")
    val aggregateRating: String,
    @SerializedName("rating_color")
    val ratingColor: String,
    @SerializedName("rating_obj")
    val ratingObj: RatingObj,
    @SerializedName("rating_text")
    val ratingText: String,
    val votes: Int
) : Parcelable