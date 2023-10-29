package com.serapbekar.fprojectsb.ui.cart

import android.os.Bundle
import android.view.View
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
import com.serapbekar.fprojectsb.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.CartProductListener {

    private val binding by viewBinding(FragmentCartBinding::bind)
    private val viewModel: CartViewModel by viewModels()
    private val cartAdapter by lazy { CartAdapter(this) }
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        with(binding) {
            rvCart.adapter = cartAdapter
            viewModel.getCartProducts(auth.currentUser?.uid.toString())
            btnClearCart.setOnClickListener {
                viewModel.clearCart(auth.currentUser?.uid.toString())
                viewModel.getCartProducts(auth.currentUser?.uid.toString())
            }
            btnPay.setOnClickListener {
                viewModel.getCartProducts(auth.currentUser?.uid.toString())
            }
        }
        onPayButtonClick()
        observeCartData()
    }

    private fun observeCartData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { cartstate ->
            when (cartstate) {
                is CartState.Loading -> {
                    progressBar.visible()
                }
                is CartState.Success -> {
                    progressBar.gone()
                    cartAdapter.submitList(cartstate.products)
                }
                is CartState.Error -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), cartstate.throwable.message.orEmpty(), 1000).show()
                }
                is CartState.Delete -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), cartstate.baseResponse.message.toString(), 1000)
                        .show()
                }
                is CartState.Clear -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), cartstate.baseResponse.message.toString(), 1000)
                        .show()
                }
            }

        }
        viewModel.amountState.observe(viewLifecycleOwner) {
            tvAmount.text = String.format(it.toString())
        }
    }
    override fun onCartDetailClick(id: Int) {
        val action = CartFragmentDirections.cartToDetail(id)
        findNavController().navigate(action)
    }

    override fun onDeleteItemClick(id: Int) {
        viewModel.deleteFromCart(id, auth.currentUser?.uid.orEmpty())
    }

    override fun onClearCartClick(userId: String) {
        binding.btnClearCart.setOnClickListener{
            viewModel.clearCart(userId)
        }
    }

    override fun onIncreaseClick(price: Double?) {
        viewModel.increase(price)
    }

    override fun onDecreaseClick(price: Double?) {
        viewModel.decrease(price)
    }

    private fun onPayButtonClick() {
        binding.btnPay.setOnClickListener {
            val action = CartFragmentDirections.cartToPayment()
            findNavController().navigate(action)
        }
    }
}
