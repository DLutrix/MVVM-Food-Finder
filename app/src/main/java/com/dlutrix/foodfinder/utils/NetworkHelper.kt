package com.dlutrix.foodfinder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
@Singleton
class NetworkHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isNetworkConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(
            networkCapabilities
        ) ?: return false

        return when {
            activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}