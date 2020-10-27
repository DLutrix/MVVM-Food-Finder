package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Parcelize
data class ShareData(
    @SerializedName("should_show")
    val shouldShow: Int
) : Parcelable