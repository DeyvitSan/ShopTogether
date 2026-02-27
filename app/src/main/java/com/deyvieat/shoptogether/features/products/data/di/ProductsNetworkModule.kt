package com.deyvieat.shoptogether.features.products.data.di

import com.deyvieat.shoptogether.core.di.AuctionRetrofit
import com.deyvieat.shoptogether.features.products.data.datasources.remote.api.ProductsApi
import dagger.Module; import dagger.Provides; import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent; import retrofit2.Retrofit; import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object ProductsNetworkModule {
    @Provides @Singleton fun provide(@AuctionRetrofit r: Retrofit): ProductsApi = r.create(ProductsApi::class.java)
}
