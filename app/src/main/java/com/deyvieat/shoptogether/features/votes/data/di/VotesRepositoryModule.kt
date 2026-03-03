package com.deyvieat.shoptogether.features.votes.data.di

import com.deyvieat.shoptogether.features.votes.data.repositories.VotesRepositoryImpl
import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VotesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVotesRepository(impl: VotesRepositoryImpl): VotesRepository
}
