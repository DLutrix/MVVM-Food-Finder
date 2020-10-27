package com.dlutrix.foodfinder.data.model


import com.google.gson.annotations.SerializedName

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
data class SearchRestaurant(
    val restaurants: List<Restaurant>,
    @SerializedName("results_found")
    val resultsFound: Int,
    @SerializedName("results_shown")
    val resultsShown: Int,
    @SerializedName("results_start")
    val resultsStart: Int
)