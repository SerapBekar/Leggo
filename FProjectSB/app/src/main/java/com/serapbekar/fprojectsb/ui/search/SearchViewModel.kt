package com.serapbekar.fprojectsb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapbekar.fprojectsb.common.Resource
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState


    fun searchProduct(query: String?){
        viewModelScope.launch {
            when(val result = productsRepository.searchProduct(query)){
                is Resource.Success -> {
                    _searchState.value = SearchState.Success(result.data)
                }
                is Resource.Error -> {
                    _searchState.value = SearchState.Error(result.throwable)
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

sealed interface SearchState {
    object Loading : SearchState
    data class Success(val product:List<ProductUI>) : SearchState
    data class Error(val throwable: Throwable) : SearchState
}