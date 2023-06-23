package com.example.swipeassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.data.NetworkResult
import com.example.swipeassignment.data.UIStatus
import com.example.swipeassignment.data.models.ProductDataDataModel
import com.example.swipeassignment.data.repository.ProductListFragmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProductListFragmentViewModel @Inject constructor(private val productListFragmentRepository: ProductListFragmentRepository): ViewModel() {

    private val productDataLiveDataPrivate =
        MutableLiveData<UIStatus<List<ProductDataDataModel>>>(UIStatus.Loading)


    val productDataLiveData : LiveData<UIStatus<List<ProductDataDataModel>>>
        get() = productDataLiveDataPrivate

    init {
        getProductData()
    }

    private fun getProductData() = viewModelScope.launch {
        productDataLiveDataPrivate.postValue(UIStatus.Loading)
        when(val productData = productListFragmentRepository.getProductData()) {
            is NetworkResult.Success -> productDataLiveDataPrivate.postValue(UIStatus.Success(productData.data))
            is NetworkResult.Failure -> productDataLiveDataPrivate.postValue(UIStatus.Error("${productData.code} ${productData.message}"))
            is NetworkResult.Exception -> productDataLiveDataPrivate.postValue(UIStatus.Error(productData.e.message ?: "Error Occurred"))
        }
    }
}