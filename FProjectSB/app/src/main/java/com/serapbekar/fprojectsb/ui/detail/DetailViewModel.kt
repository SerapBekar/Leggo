package com.serapbekar.fprojectsb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapbekar.fprojectsb.common.Resource
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.data.model.request.AddToCartRequest
import com.serapbekar.fprojectsb.data.model.response.BaseResponse
import com.serapbekar.fprojectsb.data.repository.ProductsRepository
import com.serapbekar.fprojectsb.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState>
        get() = _detailState

    fun getProductDetail(id:Int){
        viewModelScope.launch {
            when (val result = productsRepository.getProductDetail(id)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.Success(result.data)
                }
                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }
                else -> {}
            }
        }
    }

    fun addToCart(addToCartRequest: AddToCartRequest){
        viewModelScope.launch {
            when(val result = productsRepository.addToCart(addToCartRequest)){
                is Resource.Success ->  {
                    DetailState.AddCart(result.data)
                }
                is Resource.Error ->  {
                    DetailState.Error(result.throwable)
                }
                else -> {}
            }
        }
    }
}


sealed interface DetailState {
    data class Success(val products: ProductUI) : DetailState
    data class Error(val throwable: Throwable) : DetailState
    data class AddCart(val baseResponse: BaseResponse) : DetailState
}