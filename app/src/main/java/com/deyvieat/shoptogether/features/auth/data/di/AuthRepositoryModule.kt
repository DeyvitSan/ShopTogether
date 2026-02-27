package com.deyvieat.shoptogether.features.auth.data.di

import com.deyvieat.shoptogether.features.auth.data.repositories.AuthRepositoryImpl
import com.deyvieat.shoptogether.features.auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module @InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule{

    @Binds @Singleton abstract fun bind(impl: AuthRepositoryImpl): AuthRepository
}