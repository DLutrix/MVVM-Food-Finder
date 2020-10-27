package com.dlutrix.foodfinder.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Parcelize
data class RestaurantX(
    @SerializedName("all_reviews_count")
    val allReviewsCount: Int,
    val apikey: String,
    @SerializedName("average_cost_for_two")
    val averageCostForTwo: Int,
    @SerializedName("book_again_url")
    val bookAgainUrl: String,
    @SerializedName("book_form_web_view_url")
    val bookFormWebViewUrl: String,
    @SerializedName("book_url")
    val bookUrl: String,
    val cuisines: String,
    val currency: String,
    val deeplink: String,
    val establishment: List<String> = arrayListOf(),
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("featured_image")
    val featuredImage: String,
    @SerializedName("has_online_delivery")
    val hasOnlineDelivery: Int,
    @SerializedName("has_table_booking")
    val hasTableBooking: Int,
    val highlights: List<String> = arrayListOf(),
    val id: String,
    @SerializedName("include_bogo_offers")
    val includeBogoOffers: Boolean,
    @SerializedName("is_book_form_web_view")
    val isBookFormWebView: Int,
    @SerializedName("is_delivering_now")
    val isDeliveringNow: Int,
    @SerializedName("is_table_reservation_supported")
    val isTableReservationSupported: Int,
    @SerializedName("is_zomato_book_res")
    val isZomatoBookRes: Int,
    val location: Location,
    @SerializedName("medio_provider")
    val medioProvider: String,
    @SerializedName("menu_url")
    val menuUrl: String,
    @SerializedName("mezzo_provider")
    val mezzoProvider: String,
    val name: String,
    @SerializedName("opentable_support")
    val opentableSupport: Int,
    @SerializedName("phone_numbers")
    val phoneNumbers: String,
    @SerializedName("photo_count")
    val photoCount: Int,
    @SerializedName("photos_url")
    val photosUrl: String,
    @SerializedName("price_range")
    val priceRange: Int,
    @SerializedName("R")
    val r: R,
    @SerializedName("store_type")
    val storeType: String,
    @SerializedName("switch_to_order_menu")
    val switchToOrderMenu: Int,
    val thumb: String,
    val timings: String,
    val url: String,
    @SerializedName("user_rating")
    val userRating: UserRating,
) : Parcelable