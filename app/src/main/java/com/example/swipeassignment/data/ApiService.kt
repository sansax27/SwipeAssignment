package com.example.swipeassignment.data

import com.example.swipeassignment.data.models.MessageDataModel
import com.example.swipeassignment.data.models.ProductDataDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import javax.inject.Singleton


@Singleton
interface ApiService {

    @GET("get")
    suspend fun getProductData(): Response<List<ProductDataDataModel>>

    @JvmSuppressWildcards
    @Multipart
    @POST("add")
    suspend fun addProductData(@PartMap partMap: Map<String, RequestBody>,
                               @Part file: MultipartBody.Part?): Response<MessageDataModel>


}