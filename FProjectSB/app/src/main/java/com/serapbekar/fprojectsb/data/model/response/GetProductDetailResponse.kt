package com.serapbekar.fprojectsb.data.model.response

import com.serapbekar.fprojectsb.data.products.Product

data class GetProductDetailResponse(
    val message: String?,
    val product: Product,
    val status: Int?
)