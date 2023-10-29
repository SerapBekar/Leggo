package com.serapbekar.fprojectsb.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.serapbekar.fprojectsb.data.products.ProductEntity


@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    suspend fun getProducts() : List<ProductEntity>

    @Query("SELECT title FROM products")
    suspend fun getFavoriteProductTitle() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteProduct(product: ProductEntity)

    @Delete
    suspend fun deleteFavoriteProduct(product: ProductEntity)

}