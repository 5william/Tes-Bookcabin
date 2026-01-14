package com.virakumaro.testbookcabin.core

sealed class Results<out T> {
    data class Success<out T>(val data: T) : Results<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Results<Nothing>()
    object Loading : Results<Nothing>()
}