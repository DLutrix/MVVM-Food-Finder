package com.dlutrix.foodfinder.data.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("foodie_color")
    val foodieColor: String,
    @SerializedName("foodie_level")
    val foodieLevel: String,
    @SerializedName("foodie_level_num")
    val foodieLevelNum: Int,
    val name: String,
    @SerializedName("profile_deeplink")
    val profileDeeplink: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("profile_url")
    val profileUrl: String,
    @SerializedName("zomato_handle")
    val zomatoHandle: String
)