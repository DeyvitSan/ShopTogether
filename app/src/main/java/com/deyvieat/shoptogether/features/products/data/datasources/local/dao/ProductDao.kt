package com.deyvieat.shoptogether.features.products.data.datasources.local.dao

import androidx.room.*
import com.deyvieat.shoptogether.features.products.data.datasources.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao interface ProductDao {
    @Query("SELECT * FROM products WHERE roomId = :roomId ORDER BY createdAt ASC")
    fun observeByRoom(roomId: String): Flow<List<ProductEntity>>
    @Upsert suspend fun upsertAll(products: List<ProductEntity>)
}
