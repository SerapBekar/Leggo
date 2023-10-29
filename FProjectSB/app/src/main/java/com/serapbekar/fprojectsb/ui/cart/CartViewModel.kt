package com.serapbekar.fprojectsb.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.data.repository.ProductsRepository
import com.serapbekar.fprojectsb.common.Resource
import com.serapbekar.fprojectsb.data.model.request.ClearCartRequest
import com.serapbekar.fprojectsb.data.model.request.DeleteFromCartRequest
import com.serapbekar.fprojectsb.data.model.response.BaseResponse
import com.serapbekar.fprojectsb.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val productsRepository: ProductsRepository, private val userRepository: UserRepository) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState

    private var _amountState = MutableLiveData(0.0)
    val amountState: LiveData<Double> = _amountState

    fun getCartProducts(userId: String) {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            when (val result = productsRepository.getCartProducts(userId)) {
                is Resource.Success -> { _cartState.value = CartState.Success(result.data)
                    _amountState.value = result.data.sumOf {
                        if(it.saleState) {
                            it.salePrice
                        } else {
                            it.price
                        }
                    } }
                is Resource.Error -> _cartState.value = CartState.Error(result.throwable)
                else -> {}
            }
        }
    }

    fun deleteFromCart(id: Int, userId: String) {
        viewModelScope.launch {
            val deleteFromCartRequest = DeleteFromCartRequest(id)
            when(val result = productsRepository.deleteFromCart(deleteFromCartRequest)){
                is Resource.Success -> { _cartState.value = CartState.Delete(result.data); getCartProducts(userId);}
                is Resource.Error -> _cartState.value = CartState.Error(result.throwable)
                else -> {}
            }
        }
    }

    fun clearCart(userId:String){
        viewModelScope.launch {
            val clearCartRequest = ClearCartRequest(userId)
            when(val result = productsRepository.clearCart(clearCartRequest)){
                is Resource.Success -> { _cartState.value = CartState.Clear(result.data); getCartProducts(userId) }
                is Resource.Error -> _cartState.value = CartState.Error(result.throwable)
                else -> {}
            }
        }
    }

    fun increase(price: Double?) {
        _amountState.value = price?.let { _amountState.value?.plus(it) }
    }

    fun decrease(price: Double?) {
        _amountState.value = price?.let { _amountState.value?.minus(it) }
    }

    private fun resetTotalAmount() {
        _amountState.value = 0.0
    }
}

sealed interface CartState {
    object Loading : CartState
    data class Success(val products: List<ProductUI>) : CartState
    data class Error(val throwable: Throwable) : CartState
    data class Delete(val baseResponse: BaseResponse) : CartState
    data class Clear(val baseResponse: BaseResponse) : CartState
}