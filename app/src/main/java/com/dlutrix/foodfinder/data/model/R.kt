package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Parcelize
data class R(
    @SerializedName("has_menu_status")
    val hasMenuStatus: HasMenuStatus,
    @SerializedName("is_grocery_store")
    val isGroceryStore: Boolean,
    @SerializedName("res_id")
    val resId: Int
) : Parcelable