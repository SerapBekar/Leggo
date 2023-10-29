package com.serapbekar.fprojectsb.ui.detail

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.loadImage
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.common.visible
import com.serapbekar.fprojectsb.data.model.request.AddToCartRequest
import com.serapbekar.fprojectsb.databinding.FragmentDetailBinding
import com.serapbekar.fprojectsb.ui.cart.CartState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewModel: DetailViewModel by viewModels()
    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProductDetail(args.id)
        observeDetailData()
        auth = FirebaseAuth.getInstance()

        with(binding) {
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnAddCart.setOnClickListener {
                val addToCartRequest = AddToCartRequest(auth.currentUser?.uid.toString(), args.id)
                viewModel.addToCart(addToCartRequest)
                Snackbar.make(requireView(), "The product has been added successfully!", 1000).show()
            }
        }
    }

    private fun observeDetailData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { detailstate ->
            when(detailstate){
                is DetailState.Success -> {
                    tvTitle.text = detailstate.products.title
                    tvPrice.text = "${detailstate.products.price}"
                    tvCategory.text = detailstate.products.category
                    tvDescriptionAll.text = detailstate.products.description
                    ratingBar.rating = (detailstate.products.rate)?.toFloat() as Float
                    ivProduct.loadImage(detailstate.products.imageOne)
                    if (detailstate.products.saleState){
                        tvSalePrice.text = "${detailstate.products.salePrice}"
                        tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }else{
                        tvSalePrice.visible()
                    }
                }
                is DetailState.Error -> {
                    Snackbar.make(requireView(), detailstate.throwable.message.orEmpty(), 1000).show()
                }
                is DetailState.AddCart -> {
                    Snackbar.make(requireView(), detailstate.baseResponse.message.toString(), 1000).show()
                }
                else -> {}
            }
        }
    }
}