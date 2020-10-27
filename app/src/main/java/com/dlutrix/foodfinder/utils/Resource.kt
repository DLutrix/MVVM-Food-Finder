package com.dlutrix.foodfinder.utils

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val isNetworkError: Boolean?
) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(
                status = Status.SUCCESS,
                data = data,
                message = null,
                isNetworkError = null
            )
        fun <T> error(data: T?, message: String, isNetworkError: Boolean): Resource<T> =
            Resource(
                status = Status.ERROR,
                data = data,
                message = message,
                isNetworkError = isNetworkError
            )
        fun <T> loading(data: T?): Resource<T> =
            Resource(
                status = Status.LOADING,
                data = data,
                message = null,
                isNetworkError = null
            )
    }
}