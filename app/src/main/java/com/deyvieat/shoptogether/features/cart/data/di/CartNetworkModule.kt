package com.deyvieat.shoptogether.features.cart.data.di

import com.deyvieat.shoptogether.core.di.AuctionRetrofit
import com.deyvieat.shoptogether.features.cart.data.datasources.remote.api.CartApi
import dagger.Module; import dagger.Provides; import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent; import retrofit2.Retrofit; import javax.inject.Singleton
import kotlin.jvm.java


@Module @InstallIn(SingletonComponent::class)
object CartNetworkModule {
    @Provides @Singleton fun provide(@AuctionRetrofit r: Retrofit): CartApi = r.create(CartApi::class.java)
}