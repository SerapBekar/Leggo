package com.serapbekar.fprojectsb.data.products

data class Product(
    val id: Int?,
    val title: String?,
    val price: Double?,
    val salePrice: Double?,
    val description: String?,
    val category: String?,
    val imageOne: String?,
    val imageTwo: String?,
    val imageThree: String?,
    val saleState: Boolean?,
    val rate: Double?,
    val count: Int?,
    val isFavorite: Boolean = false
)
