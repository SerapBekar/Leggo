package com.serapbekar.fprojectsb.ui.favorites

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
class FavoritesViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState>
        get() = _favoritesState

    fun getFavoriteProducts() {
        viewModelScope.launch {
            _favoritesState.value = FavoritesState.Loading
            when (val result = productsRepository.getFavoriteProducts()) {
                is Resource.Success -> {
                    _favoritesState.value = FavoritesState.Success(result.data)
                }
                is Resource.Error -> {
                    _favoritesState.value = FavoritesState.Error(result.throwable)
                }
                else -> {}
            }
        }
    }

    fun deleteProductFromFavorite(product: ProductUI) {
        viewModelScope.launch {
            productsRepository.deleteProductFromFavorite(product)
            getFavoriteProducts()
        }
    }
}

sealed interface FavoritesState {
    object Loading : FavoritesState
    data class Success(val products: List<ProductUI>) : FavoritesState
    data class Error(val throwable: Throwable) : FavoritesState
}