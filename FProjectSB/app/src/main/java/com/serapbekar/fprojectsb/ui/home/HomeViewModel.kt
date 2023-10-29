package com.serapbekar.fprojectsb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.data.repository.ProductsRepository
import com.serapbekar.fprojectsb.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getProducts() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            when (val result = productsRepository.getProducts()) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Success(result.data)
                }
                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
                else -> {}
            }
        }
    }

    fun getSaleProducts() {
        viewModelScope.launch {

            when(val result = productsRepository.getSaleProducts()) {
                is Resource.Success -> {
                    _homeState.value = HomeState.SaleSuccess(result.data)
                }
                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
                else -> {}
            }
        }
    }

    fun addToFavorites(product: ProductUI) {
        viewModelScope.launch {
            productsRepository.addToFavorites(product)
        }
    }
}

sealed interface HomeState {
    object Loading : HomeState
    data class Success(val products: List<ProductUI>) : HomeState
    data class SaleSuccess(val products: List<ProductUI>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
}