package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Parcelize
data class PhotoX(
    val id: Int,
    val md5sum: String,
    val order: Int,
    @SerializedName("photo_id")
    val photoId: Int,
    @SerializedName("thumb_url")
    val thumbUrl: String,
    val type: String,
    val url: String,
    val uuid: Long
) : Parcelable