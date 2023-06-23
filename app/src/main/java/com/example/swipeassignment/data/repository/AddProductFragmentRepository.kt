package com.example.swipeassignment.data.repository

import com.example.swipeassignment.data.ApiService
import com.example.swipeassignment.data.NetworkResult
import com.example.swipeassignment.data.NetworkUtils
import com.example.swipeassignment.data.models.MessageDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AddProductFragmentRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addProductData(productMap: Map<String, RequestBody>,
    image: MultipartBody.Part?): NetworkResult<MessageDataModel> =
        NetworkUtils.callApi {
            apiService.addProductData(productMap, image)
        }

}