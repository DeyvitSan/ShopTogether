package com.deyvieat.shoptogether.features.rooms.data.di

import com.deyvieat.shoptogether.core.di.AuthRetrofit
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.api.RoomsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomsNetworkModule {
    @Provides
    @Singleton
    fun provideRoomsApi(@AuthRetrofit r: Retrofit): RoomsApi = r.create(RoomsApi::class.java)
}
