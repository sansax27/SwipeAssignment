package com.example.swipeassignment.data.repository

import com.example.swipeassignment.data.ApiService
import com.example.swipeassignment.data.NetworkResult
import com.example.swipeassignment.data.NetworkUtils
import com.example.swipeassignment.data.models.ProductDataDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainActivityRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getOpenInAppData(): NetworkResult<List<ProductDataDataModel>> =
        withContext(Dispatchers.IO) { NetworkUtils.callApi { apiService.getProductData() } }

}