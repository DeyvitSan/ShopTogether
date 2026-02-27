package com.deyvieat.shoptogether.features.cart.domain.repositories

import com.deyvieat.shoptogether.features.cart.domain.entities.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun observeCart(userId: String): Flow<List<CartItem>>
    suspend fun refreshCart(userId: String): Result<Unit>
}