package com.example.swipeassignment.data

sealed class UIStatus<out T> {

    data class Success<T : Any>(val data: T) : UIStatus<T>()
    data class Error<T : Any>(val message: String) : UIStatus<T>()
    object Loading: UIStatus<Nothing>()
    object Empty: UIStatus<Nothing>()
}
