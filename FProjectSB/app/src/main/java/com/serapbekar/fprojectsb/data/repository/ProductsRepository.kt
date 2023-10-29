package com.serapbekar.fprojectsb.data.repository


import com.serapbekar.fprojectsb.common.Resource
import com.serapbekar.fprojectsb.data.mapper.mapToProductEntity
import com.serapbekar.fprojectsb.data.mapper.mapToProductUI
import com.serapbekar.fprojectsb.data.model.request.AddToCartRequest
import com.serapbekar.fprojectsb.data.model.response.BaseResponse
import com.serapbekar.fprojectsb.data.model.request.ClearCartRequest
import com.serapbekar.fprojectsb.data.model.request.DeleteFromCartRequest
import com.serapbekar.fprojectsb.data.products.ProductUI
import com.serapbekar.fprojectsb.data.source.local.ProductDao
import com.serapbekar.fprojectsb.data.source.remote.ProductService


class ProductsRepository(private val productService: ProductService, private val productDao: ProductDao) {

    suspend fun getProducts(): Resource<List<ProductUI>> {
            return try {
                val result = productService.getProducts().products
                val favoriteProductList = getFavoriteProductList()

                if (result.isNullOrEmpty()){
                    Resource.Error(Exception("Product not found!"))
                }else{
                    Resource.Success(result.map { it.mapToProductUI(isFavorite = favoriteProductList.contains(it.title)) })
                }
            } catch (e : Exception) {
                Resource.Error(e)
            }
        }

    suspend fun getProductDetail(id: Int): Resource<ProductUI> {
        return try {
            val favoriteProductList = getFavoriteProductList()
            val result = productService.getProductDetail(id).product

            result.let {
                Resource.Success(it.mapToProductUI(isFavorite = favoriteProductList.contains(it.title)))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun addToFavorites(product: ProductUI) {
        productDao.addFavoriteProduct(product.mapToProductEntity())
    }

    suspend fun deleteProductFromFavorite(product: ProductUI) {
        productDao.deleteFavoriteProduct(product.mapToProductEntity())
    }

    suspend fun clearCart(clearCartRequest: ClearCartRequest) : Resource<BaseResponse> {
        return try {
            val result = productService.clearCart(clearCartRequest)

            if(result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception(result.message.toString()))
            }
        } catch(e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun addToCart(addToCartRequest: AddToCartRequest) : Resource<BaseResponse> {
        return try {
            val result = productService.addToCart(addToCartRequest)

            if(result.status==200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception(result.message.toString()))
            }
        } catch(e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteFromCart(deleteFromCartRequest: DeleteFromCartRequest) : Resource<BaseResponse> {
        return try {
            val result = productService.deleteFromCart(deleteFromCartRequest)

            if(result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception(result.message.toString()))
            }
        } catch(e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getCartProducts(userId:String?) : Resource<List<ProductUI>> {
        return try {
            val favoriteProductList = getFavoriteProductList()
            val result = productService.getCartProducts(userId)

            if(result.status == 200) {
                Resource.Success(result.products.orEmpty().map {
                    it.mapToProductUI(isFavorite = favoriteProductList.contains(it.title))
                })
            } else {
                Resource.Error(Exception(result.message.orEmpty()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getSaleProducts(): Resource<List<ProductUI>> {
            return try {
                val favoriteNamesList = getFavoriteProductList()
                val result = productService.getSaleProducts().products.orEmpty()

                if (result.isEmpty()) {
                    Resource.Error(Exception("Sale product are not found!"))
                } else {
                    Resource.Success(result.map {
                        it.mapToProductUI(isFavorite = favoriteNamesList.contains(it.title))
                    })
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    suspend fun searchProduct(query:String?) : Resource<List<ProductUI>> {
            return try {
                val favoriteNamesList = getFavoriteProductList()
                val result = productService.searchProduct(query).products
                result?.let{
                    Resource.Success(result.map {
                        it.mapToProductUI(isFavorite = favoriteNamesList.contains(it.title))
                    })
                } ?: kotlin.run {
                    Resource.Error(Exception("There is no such a product!"))
                }
            } catch(e:Exception) {
                Resource.Error(e)
            }
        }

    suspend fun getFavoriteProducts(): Resource<List<ProductUI>> {
            return try {
                val result = productDao.getProducts().map { it.mapToProductUI() }

                if (result.isEmpty()) {
                    Resource.Error(Exception("There are no products here!"))
                } else {
                    Resource.Success(result)
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    suspend fun getFavoriteProductList() = productDao.getFavoriteProductTitle()
}