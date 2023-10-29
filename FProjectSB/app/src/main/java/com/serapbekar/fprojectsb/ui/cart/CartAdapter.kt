package com.serapbekar.fprojectsb.ui.cart

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.loadImage
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.databinding.ItemCartProductBinding

class CartAdapter(
    private  val productListener: CartProductListener
    ) : ListAdapter<ProductUI, CartAdapter.CartProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder =
        CartProductViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) { holder.bind(getItem(position))
    }

    class CartProductViewHolder(
        private val binding: ItemCartProductBinding,
        private val productListener: CartProductListener
    ):RecyclerView.ViewHolder(binding.root) {


        fun bind(product : ProductUI) = with(binding){
            var productPiece = 1
            tvTitle.text = product.title
            ivProducts.loadImage(product.imageOne)
            tvPrice.text = "${product.price} ₺"
            tvPiece.text = productPiece.toString()
            if (product.saleState){
                tvSalePrice.text = "${product.salePrice} ₺"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                tvSalePrice.gone()
            }
            root.setOnClickListener {
                productListener.onCartDetailClick(product.id)
            }
            btnDelete.setOnClickListener {
                productListener.onDeleteItemClick(product.id)
            }
            ibAdd.setOnClickListener {
                productListener.onIncreaseClick(product.price)
                productPiece++
                tvPiece.text = productPiece.toString()
            }
            ibRemove.setOnClickListener {
                if (productPiece != 1) {
                    productListener.onDecreaseClick(product.price)
                    productPiece--
                    tvPiece.text = productPiece.toString()
                } else{
                    productListener.onDeleteItemClick(product.id)
                }
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

    interface CartProductListener {
        fun onCartDetailClick(id:Int)
        fun onDeleteItemClick(id:Int)
        fun onClearCartClick(userId:String)
        fun onIncreaseClick(price:Double?)
        fun onDecreaseClick(price:Double?)
    }
}