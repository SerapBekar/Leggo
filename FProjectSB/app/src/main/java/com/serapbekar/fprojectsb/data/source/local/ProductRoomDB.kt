package com.serapbekar.fprojectsb.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serapbekar.fprojectsb.data.products.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductRoomDB : RoomDatabase(){
    abstract fun productDao() : ProductDao
}
