package com.ebdz.ble.data

import android.support.annotation.StringRes

/**
 * A generic class that describes data with a status.
 */
sealed class Resource<out T>(val data: T? = null) {
    class StartingResource<out T> : Resource<T>()

    class Stopped<out T> : Resource<T>()

    class Stopping<out T> : Resource<T>()

    class SuccessResource<out T>(data: T) : Resource<T>(data)

    data class ErrorResource<out T>(@StringRes val errorMessage: Int) : Resource<T>()

    data class ErrorResourceString<out T>(@StringRes val errorMessage: String) : Resource<T>()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data?.hashCode() ?: 0
    }
}