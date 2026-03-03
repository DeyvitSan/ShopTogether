package com.deyvieat.shoptogether.features.votes.data.di

import com.deyvieat.shoptogether.core.di.AuthRetrofit
import com.deyvieat.shoptogether.features.votes.data.datasources.remote.api.VotesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VotesNetworkModule {

    @Provides
    @Singleton
    fun provideVotesApi(@AuthRetrofit retrofit: Retrofit): VotesApi =
        retrofit.create(VotesApi::class.java)
}
