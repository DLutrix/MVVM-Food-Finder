package com.dlutrix.foodfinder.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
data class Location(
    val address: String,
    val city: String,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("country_id")
    val countryId: Int,
    val latitude: String,
    val locality: String,
    @SerializedName("locality_verbose")
    val localityVerbose: String,
    val longitude: String,
    val zipcode: String
) : Serializable