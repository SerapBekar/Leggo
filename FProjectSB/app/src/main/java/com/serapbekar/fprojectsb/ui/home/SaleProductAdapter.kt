import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serapbekar.fprojectsb.common.loadImage
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.databinding.ItemSaleProductBinding

    class SaleProductAdapter(
        private  val saleProductListener: SaleProductListener
    ) : ListAdapter<ProductUI, SaleProductAdapter.SaleProductViewHolder>(ProductDiffCallBack()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleProductViewHolder =
            SaleProductViewHolder(
                ItemSaleProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                saleProductListener
            )


        override fun onBindViewHolder(holder: SaleProductViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        class SaleProductViewHolder(
            private val binding: ItemSaleProductBinding,
            private val saleProductListener: SaleProductListener
        ):RecyclerView.ViewHolder(binding.root) {

            fun bind(product : ProductUI) = with(binding){
                tvTitle.text = product.title
                ivProduct.loadImage(product.imageOne)
                tvPrice.text = "${product.price} ₺"
                tvRate.text = "${product.rate.toFloat()}"
                if (product.saleState == true){
                    tvSalePrice.text = "${product.salePrice} ₺"
                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                var isLike = product.isFavorite
                lottieFavorite.setOnClickListener {
                    isLike = !isLike
                    lottieFavorite.apply {
                        if(isLike) {
                            saleProductListener.onFavoriteProductClick(product)
                            playAnimation()
                        }else{
                            cancelAnimation()
                        }
                    }
                }

                root.setOnClickListener {
                    saleProductListener.onSaleProductClick(product.id)
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

    interface SaleProductListener {
       fun onSaleProductClick(id:Int)
       fun onFavoriteProductClick(product: ProductUI)
    }
}

