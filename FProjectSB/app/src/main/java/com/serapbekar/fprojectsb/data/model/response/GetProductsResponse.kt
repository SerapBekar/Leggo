package com.serapbekar.fprojectsb.data.model.response

import com.serapbekar.fprojectsb.data.products.Product


data class GetProductsResponse(
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)