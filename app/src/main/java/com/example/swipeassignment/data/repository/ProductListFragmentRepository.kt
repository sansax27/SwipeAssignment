package com.example.swipeassignment.data.repository

import com.example.swipeassignment.data.ApiService
import com.example.swipeassignment.data.NetworkResult
import com.example.swipeassignment.data.NetworkUtils
import com.example.swipeassignment.data.models.ProductDataDataModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProductListFragmentRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProductData(): NetworkResult<List<ProductDataDataModel>> =
        NetworkUtils.callApi { apiService.getProductData() }

}