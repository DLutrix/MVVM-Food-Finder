package com.dlutrix.foodfinder.utils

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
object Constant {

    const val DEBUG = true

    private const val API_KEY = "API_KEY "
    const val HEADER_1 = "Accept: application/json"
    const val HEADER_2 = "user-key: $API_KEY"
    const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"

    const val SHARED_PREFERENCES_NAME = "sharedPref"
    const val KEY_LAT = "KEY_LAT"
    const val KEY_LONG = "KEY_LONG"
    const val KEY_FIRST_TIME = "KEY_FIRST_TIME"

    const val STARTING_PAGE_COUNT = 0

    const val DEFAULT_LAT = "-6.173110"
    const val DEFAULT_LONG = "106.829361"
}