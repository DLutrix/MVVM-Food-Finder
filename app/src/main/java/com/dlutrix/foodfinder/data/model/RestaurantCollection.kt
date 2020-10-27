package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
data class RestaurantCollection(
    val collections: List<Collection>,
    @SerializedName("display_text")
    val displayText: String?,
    @SerializedName("has_more")
    val hasMore: Int?,
    @SerializedName("has_total")
    val hasTotal: Int?,
    @SerializedName("share_url")
    val shareUrl: String?,
    @SerializedName("user_has_addresses")
    val userHasAddresses: Boolean?
)