package com.serapbekar.fprojectsb.di

import com.serapbekar.fprojectsb.data.repository.ProductsRepository
import com.serapbekar.fprojectsb.data.source.local.ProductDao
import com.serapbekar.fprojectsb.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(productService: ProductService, productDao: ProductDao): ProductsRepository =
        ProductsRepository(productService, productDao)
}
