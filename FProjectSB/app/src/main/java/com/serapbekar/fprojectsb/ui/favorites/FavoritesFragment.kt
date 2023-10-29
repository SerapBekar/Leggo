package com.serapbekar.fprojectsb.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.common.visible
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.databinding.FragmentFavoritesBinding
import com.serapbekar.fprojectsb.ui.detail.DetailState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites), FavoritesAdapter.FavoriteListener {

    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private val viewModel: FavoritesViewModel by viewModels()
    private val favoritesAdapter by lazy { FavoritesAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavorites.adapter = favoritesAdapter
        viewModel.getFavoriteProducts()
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.favoritesState.observe(viewLifecycleOwner) {favoritestate ->
            when(favoritestate){
                FavoritesState.Loading -> {
                    progressBar.visible()
                }
                is FavoritesState.Success -> {
                    progressBar.gone()
                    favoritesAdapter.submitList(favoritestate.products)
                }
                is FavoritesState.Error -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), favoritestate.throwable.message.orEmpty(), 1000).show()
                    rvFavorites.gone()
                }
                else -> {}
            }
        }
    }

    override fun onFavoriteProductClick(product: ProductUI) {
        viewModel.deleteProductFromFavorite(product)
    }
}