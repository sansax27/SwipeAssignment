package com.example.swipeassignment.data

import retrofit2.Response


object NetworkUtils {

    suspend fun <T : Any> callApi(
        executeApi: suspend () ->  Response<T>
    ): NetworkResult<T> {
        return try {
            val response = executeApi()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Failure(response.code(), response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}