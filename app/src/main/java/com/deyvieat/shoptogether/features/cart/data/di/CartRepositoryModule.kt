package com.deyvieat.shoptogether.features.cart.data.di

import com.deyvieat.shoptogether.features.cart.data.repositories.CartRepositoryImpl
import com.deyvieat.shoptogether.features.cart.domain.repositories.CartRepository
import dagger.Binds; import dagger.Module; import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent; import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {
    @Binds @Singleton abstract fun bind(impl: CartRepositoryImpl): CartRepository
}