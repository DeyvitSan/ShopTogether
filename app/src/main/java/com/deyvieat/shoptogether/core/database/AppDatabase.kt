package com.deyvieat.shoptogether.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.deyvieat.shoptogether.features.rooms.data.datasources.local.dao.RoomDao
import com.deyvieat.shoptogether.features.rooms.data.datasources.local.entities.RoomEntity

import com.deyvieat.shoptogether.features.products.data.datasources.local.dao.ProductDao
import com.deyvieat.shoptogether.features.products.data.datasources.local.entities.ProductEntity

import com.deyvieat.shoptogether.features.cart.data.datasources.local.dao.CartDao
import com.deyvieat.shoptogether.features.cart.data.datasources.local.entities.CartEntity

import com.deyvieat.shoptogether.features.votes.data.datasources.local.dao.VoteDao
import com.deyvieat.shoptogether.features.votes.data.datasources.local.entities.VoteEntity

@Database(
    entities = [
        RoomEntity::class,
        ProductEntity::class,
        CartEntity::class,
        VoteEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun voteDao(): VoteDao
}
