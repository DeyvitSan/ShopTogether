package com.deyvieat.shoptogether.features.rooms.data.di

import com.deyvieat.shoptogether.features.rooms.data.repositories.RoomsRepositoryImpl
import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository
import dagger.Binds; import dagger.Module; import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent; import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
abstract class RoomsRepositoryModule {
    @Binds @Singleton abstract fun bind(impl: RoomsRepositoryImpl): RoomsRepository
}
