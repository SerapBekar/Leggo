package com.serapbekar.fprojectsb.ui.search

import ProductsAdapter
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), ProductsAdapter.ProductListener {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by viewModels<SearchViewModel>()
    private val productsAdapter by lazy { ProductsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
                rvSearchProducts.adapter = productsAdapter
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(query: String?): Boolean {
                    viewModel.searchProduct(query)
                    return true
                }
            })
        }
        observeData()
    }

    private fun observeData() {
        viewModel.searchState.observe(viewLifecycleOwner) { searchState ->
            when(searchState) {
                is SearchState.Loading -> {
                    ProgressBar.VISIBLE
                }
                is SearchState.Success -> {
                    productsAdapter.submitList(searchState.product)
                    ProgressBar.GONE
                }
                is SearchState.Error -> {
                    Snackbar.make(requireView(), searchState.throwable.message.toString(), 1000).show()
                    ProgressBar.GONE
                }
                else -> {}
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = SearchFragmentDirections.searchToDetail(id)
        findNavController().navigate(action)
    }

    override fun onFavoriteProductClick(product: ProductUI) {
        viewModel.addToFavorites(product)
    }
}