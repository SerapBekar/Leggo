package com.serapbekar.fprojectsb.ui.success

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.data.repository.ProductsRepository
import com.serapbekar.fprojectsb.data.repository.UserRepository
import com.serapbekar.fprojectsb.common.Resource
import com.serapbekar.fprojectsb.data.model.request.ClearCartRequest
import com.serapbekar.fprojectsb.data.model.response.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentSuccessViewModel @Inject constructor(
private val productsRepository: ProductsRepository,
private val userRepository: UserRepository): ViewModel() {

    private var _paySuccessState = MutableLiveData<PaySuccessState>()
    val paySuccessState: LiveData<PaySuccessState>
    get() = _paySuccessState

    private val _amountState = MutableLiveData(0.0)
    val amountState : LiveData<Double> = _amountState

    private fun getCartProduct(userId: String?){
        viewModelScope.launch {

            when(val result = productsRepository.getCartProducts(userId)){
                is Resource.Success -> {
                    _paySuccessState.value = PaySuccessState.Success(result.data)
                    _amountState.value = result.data.sumOf { it.price ?: 0.0}
                }
                is Resource.Error -> {
                    _paySuccessState.value = PaySuccessState.Error(result.throwable)
                    _amountState.value = 0.0
                }
                else -> {}
            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            val clearCartRequest = ClearCartRequest(userId)
            when(val result = productsRepository.clearCart(clearCartRequest)) {
                is Resource.Success -> {
                    _paySuccessState.value = PaySuccessState.ClearCart(result.data)
                    getCartProduct(userRepository.getFireUserUid())
                }
                is Resource.Error -> {
                    _paySuccessState.value = PaySuccessState.Error(result.throwable)
                    updateAmount()
                }
                else -> {}
            }
        }
    }

    private fun updateAmount() {
        _amountState.value = 0.0
    }
}

sealed interface PaySuccessState {
    data class Success(val product:List<ProductUI>) : PaySuccessState
    data class Error(val throwable: Throwable) : PaySuccessState
    data class ClearCart(val baseResponse: BaseResponse) : PaySuccessState
}

