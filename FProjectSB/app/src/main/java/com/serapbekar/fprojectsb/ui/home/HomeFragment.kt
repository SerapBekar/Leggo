package com.serapbekar.fprojectsb.ui.home

import ProductsAdapter
import SaleProductAdapter
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.common.visible
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ProductsAdapter.ProductListener, SaleProductAdapter.SaleProductListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private val productsAdapter by lazy { ProductsAdapter(this) }
    private val saleProductAdapter by lazy { SaleProductAdapter(this) }
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSale.adapter = saleProductAdapter
        binding.rvAll.adapter = productsAdapter
        auth = Firebase.auth
        viewModel.getProducts()
        viewModel.getSaleProducts()
        observeData()
        logOut()
    }

    private fun observeData(){
        with(binding){
            viewModel.homeState.observe(viewLifecycleOwner) { homeState ->
                when(homeState){
                    HomeState.Loading -> {
                        ProgressBar.VISIBLE
                    }
                    is HomeState.Success -> {
                        ProgressBar.GONE
                        productsAdapter.submitList(homeState.products)
                    }
                    is HomeState.SaleSuccess-> {
                        ProgressBar.GONE
                        saleProductAdapter.submitList(homeState.products)
                    }
                    is HomeState.Error -> {
                        ProgressBar.GONE
                        Snackbar.make(requireView(), homeState.throwable.message.orEmpty(), 1000).show()  //Liste boş ise yada hata aldıysam
                        rvAll.gone()
                        rvSale.gone()
                    }
                }
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = HomeFragmentDirections.homeToDetail(id)
        findNavController().navigate(action)
    }

    override fun onFavoriteProductClick(product: ProductUI) {
        viewModel.addToFavorites(product)
    }

    override fun onSaleProductClick(id: Int) {
        val action = HomeFragmentDirections.homeToDetail(id)
        findNavController().navigate(action)
    }
    private fun logOut() {
        val showPopUp = PopupMenu(
            context,
            binding.ivProfile
        )

        showPopUp.menu.add(Menu.NONE, 0, 0, "Log Out")

        showPopUp.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                FirebaseAuth.getInstance().signOut()
                val action = HomeFragmentDirections.homeToSignIn()
                findNavController().navigate(action)
            }
            false
        }

        binding.ivProfile.setOnClickListener {
            showPopUp.show()
        }
    }
}