package com.serapbekar.fprojectsb.ui.favorites

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.loadImage
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.databinding.ItemProductsBinding

class FavoritesAdapter(private val favoriteListener: FavoriteListener
) : ListAdapter<ProductUI, FavoritesAdapter.FavoriteViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            favoriteListener
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) { holder.bind(getItem(position))
    }

    class FavoriteViewHolder(
        private val binding: ItemProductsBinding,
        private val favoriteListener: FavoriteListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            ivProducts.loadImage(product.imageOne)
            tvPrice.text = "${product.price} ₺"
            tvRate.text = "${product.rate.toFloat()}"
            if (product.saleState) {
                tvSalePrice.text = "${product.salePrice} ₺"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvSalePrice.gone()
            }
            lottieFavorite.setOnClickListener {
                favoriteListener.onFavoriteProductClick(product)
                lottieFavorite.playAnimation()
            }
        }
    }


    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface FavoriteListener {
        fun onFavoriteProductClick(product: ProductUI)
    }
}
