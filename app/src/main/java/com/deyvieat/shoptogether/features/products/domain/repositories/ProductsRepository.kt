package com.deyvieat.shoptogether.features.products.domain.repositories

import com.deyvieat.shoptogether.features.products.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun observeProducts(roomId: String): Flow<List<Product>>
    suspend fun refreshProducts(roomId: String): Result<Unit>
    suspend fun createProduct(
        roomId: String,
        name: String,
        price: Double,
        stock: Int,
        imageUrl: String
    ): Result<Product>
}