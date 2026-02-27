package com.deyvieat.shoptogether.core.database


import android.content.Context
import androidx.room.Room
import com.deyvieat.shoptogether.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shoptogether_db"
        ).build()
    }

    @Provides
    fun provideRoomDao(db: AppDatabase) = db.roomDao()

    @Provides
    fun provideProductDao(db: AppDatabase) = db.productDao()

    @Provides
    fun provideVoteDao(db: AppDatabase) = db.voteDao()

    @Provides
    fun provideCartDao(db: AppDatabase) = db.cartDao()
}