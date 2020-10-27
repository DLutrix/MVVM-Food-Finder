package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
data class CollectionX(
    @SerializedName("collection_id")
    val collectionId: Int?,
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("res_count")
    val resCount: Int?,
    @SerializedName("share_url")
    val shareUrl: String?,
    val title: String?,
    val url: String?
)