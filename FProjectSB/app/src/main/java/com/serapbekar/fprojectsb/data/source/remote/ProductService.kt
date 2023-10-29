package com.serapbekar.fprojectsb.data.source.remote

import com.serapbekar.fprojectsb.common.Contants.EndPoints.ADD_TO_CART
import com.serapbekar.fprojectsb.common.Contants.EndPoints.DELETE_FROM_CART
import com.serapbekar.fprojectsb.common.Contants.EndPoints.GET_CART_PRODUCTS
import com.serapbekar.fprojectsb.common.Contants.EndPoints.GET_PRODUCTS
import com.serapbekar.fprojectsb.common.Contants.EndPoints.GET_SALE_PRODUCTS
import com.serapbekar.fprojectsb.common.Contants.EndPoints.SEARCH_PRODUCT
import com.serapbekar.fprojectsb.common.Contants.EndPoints.GET_PRODUCT_DETAIL
import com.serapbekar.fprojectsb.common.Contants.EndPoints.CLEAR_CART
import com.serapbekar.fprojectsb.data.model.request.AddToCartRequest
import com.serapbekar.fprojectsb.data.model.response.BaseResponse
import com.serapbekar.fprojectsb.data.model.request.ClearCartRequest
import com.serapbekar.fprojectsb.data.model.request.DeleteFromCartRequest
import com.serapbekar.fprojectsb.data.model.response.GetCartProductsResponse
import com.serapbekar.fprojectsb.data.model.response.GetProductDetailResponse
import com.serapbekar.fprojectsb.data.model.response.GetProductsResponse
import com.serapbekar.fprojectsb.data.model.response.GetSaleProducts
import com.serapbekar.fprojectsb.data.model.response.SearchProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {
    @Headers("store: leggo")
    @GET(GET_PRODUCTS)
    suspend fun getProducts(): GetProductsResponse

    @Headers("store: leggo")
    @GET(GET_SALE_PRODUCTS)
    suspend fun getSaleProducts() : GetSaleProducts

    @Headers("store: leggo")
    @GET(GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(@Query("id") id: Int) : GetProductDetailResponse

    @Headers("store: leggo")
    @GET(SEARCH_PRODUCT)
    suspend fun searchProduct(@Query("query") query: String?) : SearchProductResponse

    @Headers("store: leggo")
    @POST(ADD_TO_CART)
    suspend fun addToCart(@Body addToCartRequest: AddToCartRequest) : BaseResponse

    @Headers("store: leggo")
    @GET(GET_CART_PRODUCTS)
    suspend fun getCartProducts(@Query("userId") userId : String?) : GetCartProductsResponse

    @Headers("store: leggo")
    @POST(DELETE_FROM_CART)
    suspend fun deleteFromCart (@Body deleteFromCartRequest: DeleteFromCartRequest) : BaseResponse

    @Headers("store: leggo")
    @POST(CLEAR_CART)
    suspend fun clearCart(@Body clearCartRequest: ClearCartRequest) : BaseResponse
}