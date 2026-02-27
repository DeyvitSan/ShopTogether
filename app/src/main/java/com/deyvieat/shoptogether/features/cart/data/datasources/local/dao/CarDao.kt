package com.deyvieat.shoptogether.features.cart.data.datasources.local.dao

import androidx.room.*
import com.deyvieat.shoptogether.features.cart.data.datasources.local.entities.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao interface CartDao {
    @Query("SELECT * FROM cart WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeCart(userId: String): Flow<List<CartEntity>>
    @Upsert suspend fun upsertAll(items: List<CartEntity>)
}
