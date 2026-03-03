package com.deyvieat.shoptogether.features.auth.data.di

import com.deyvieat.shoptogether.core.di.AuthRetrofit
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(@AuthRetrofit retrofit: Retrofit): AuthApi = 
        retrofit.create(AuthApi::class.java)
}
