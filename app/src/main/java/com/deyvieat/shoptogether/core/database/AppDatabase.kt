package com.deyvieat.shoptogether.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deyvieat.shoptogether.features.rooms.data.local.dao.RoomDao
import com.deyvieat.shoptogether.features.rooms.data.local.entities.RoomEntity
import com.deyvieat.shoptogether.features.products.data.local.dao.ProductDao
import com.deyvieat.shoptogether.features.products.data.local.entities.ProductEntity
import com.deyvieat.shoptogether.features.votes.data.local.dao.VoteDao
import com.deyvieat.shoptogether.features.votes.data.local.entities.VoteEntity
import com.deyvieat.shoptogether.features.cart.data.local.dao.CartDao
import com.deyvieat.shoptogether.features.cart.data.local.entities.CartEntity

@Database(
    entities = [
        RoomEntity::class,
        ProductEntity::class,
        VoteEntity::class,
        CartEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao
    abstract fun productDao(): ProductDao
    abstract fun voteDao(): VoteDao
    abstract fun cartDao(): CartDao
}