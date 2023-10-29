package com.serapbekar.fprojectsb.data.mapper

import com.serapbekar.fprojectsb.data.products.Product
import com.serapbekar.fprojectsb.data.products.ProductEntity
import com.serapbekar.fprojectsb.data.products.ProductUI


fun Product.mapToProductUI(isFavorite: Boolean): ProductUI {
    return ProductUI(
        id = id ?: 1,
        title = title.orEmpty(),
        price = price ?: 0.0,
        salePrice = salePrice ?: 0.0,
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageOne = imageOne.orEmpty(),
        imageTwo = imageTwo.orEmpty(),
        imageThree = imageThree.orEmpty(),
        rate = rate ?: 0.0,
        count = count ?: 1,
        saleState = saleState ?: false,
        isFavorite = isFavorite
    )
}

fun ProductUI.mapToProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        salePrice = salePrice,
        description = description,
        category = category,
        imageOne = imageOne,
        imageTwo = imageTwo,
        imageThree = imageThree,
        rate = rate,
        count = count,
        saleState = saleState,
    )
}

fun ProductEntity.mapToProductUI() : ProductUI {
    return ProductUI(
        id = id ?: 1,
        title = title.orEmpty(),
        price = price ?: 0.0,
        salePrice = salePrice ?: 0.0,
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageOne = imageOne.orEmpty(),
        imageTwo = imageTwo.orEmpty(),
        imageThree = imageThree.orEmpty(),
        rate = rate ?: 0.0,
        count = count ?: 1,
        saleState = saleState ?: false,
        isFavorite = true
    )
}