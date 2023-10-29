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

class ProductsAdapter(
    private  val productsListener: ProductListener
) : ListAdapter<ProductUI, ProductsAdapter.ProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productsListener
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        private val binding: ItemProductsBinding,
        private val productsListener: ProductListener
    ):RecyclerView.ViewHolder(binding.root) {

        fun bind(product : ProductUI) = with(binding){

            tvTitle.text = product.title
            ivProducts.loadImage(product.imageOne)
            tvPrice.text = "${product.price} ₺"
            tvRate.text = "${product.rate.toFloat()}"

            if (product.saleState){
                tvSalePrice.text = "${product.salePrice} ₺"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else{
            tvSalePrice.gone()
            }

            root.setOnClickListener {
                productsListener.onProductClick(product.id)
            }

            var isLike = product.isFavorite
            lottieFavorite.setOnClickListener {
                isLike = !isLike
                lottieFavorite.apply {
                    if(isLike) {
                        productsListener.onFavoriteProductClick(product)
                        playAnimation()
                    }else{
                        cancelAnimation()
                    }
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

    interface ProductListener {
        fun onProductClick(id:Int)
        fun onFavoriteProductClick(product: ProductUI)
    }
}