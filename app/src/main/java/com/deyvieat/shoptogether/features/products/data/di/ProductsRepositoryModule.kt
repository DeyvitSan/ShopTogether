package com.deyvieat.shoptogether.features.products.data.di

import com.deyvieat.shoptogether.features.products.data.repositories.ProductsRepositoryImpl
import com.deyvieat.shoptogether.features.products.domain.repositories.ProductsRepository
import dagger.Binds; import dagger.Module; import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent; import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
abstract class ProductsRepositoryModule {
    @Binds @Singleton abstract fun bind(impl: ProductsRepositoryImpl): ProductsRepository
}