package com.example.swipeassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.data.NetworkResult
import com.example.swipeassignment.data.UIStatus
import com.example.swipeassignment.data.models.MessageDataModel
import com.example.swipeassignment.data.repository.AddProductFragmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@HiltViewModel
class AddProductFragmentViewModel @Inject
constructor(private val addProductFragmentRepository: AddProductFragmentRepository) : ViewModel() {

    private val addProductDataLiveDataPrivate =
        MutableLiveData<UIStatus<MessageDataModel>>(UIStatus.Empty)


    val addProductDataLiveData : LiveData<UIStatus<MessageDataModel>>
        get() = addProductDataLiveDataPrivate


    fun addProductData(productName: String, productType: String, productPrice: String, productTax: String, image: MultipartBody.Part?) = viewModelScope.launch {
        addProductDataLiveDataPrivate.postValue(UIStatus.Loading)
        val dataMap = mapOf<String, RequestBody>("product_name" to productName.toRequestBody(
            MultipartBody.FORM
        ),
        "product_type" to productType.toRequestBody(MultipartBody.FORM),
        "price" to productPrice.toRequestBody(MultipartBody.FORM),
        "tax" to productTax.toRequestBody(MultipartBody.FORM)
        )
        when(val productData = addProductFragmentRepository.addProductData(dataMap, image)) {
            is NetworkResult.Success -> addProductDataLiveDataPrivate.postValue(UIStatus.Success(productData.data))
            is NetworkResult.Failure -> addProductDataLiveDataPrivate.postValue(UIStatus.Error("${productData.code} ${productData.message}"))
            is NetworkResult.Exception -> addProductDataLiveDataPrivate.postValue(UIStatus.Error(productData.e.message ?: "Error Occurred"))
        }
    }

}