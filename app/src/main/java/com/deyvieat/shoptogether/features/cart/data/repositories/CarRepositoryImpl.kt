package com.deyvieat.shoptogether.features.cart.data.repositories

import com.deyvieat.shoptogether.features.cart.data.datasources.local.dao.CartDao
import com.deyvieat.shoptogether.features.cart.data.datasources.remote.api.CartApi
import com.deyvieat.shoptogether.features.cart.data.datasources.remote.mapper.toDomain
import com.deyvieat.shoptogether.features.cart.data.datasources.remote.mapper.toEntity
import com.deyvieat.shoptogether.features.cart.domain.entities.CartItem
import com.deyvieat.shoptogether.features.cart.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow; import kotlinx.coroutines.flow.map; import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val api: CartApi, private val dao: CartDao) : CartRepository {

    override fun observeCart(userId: String): Flow<List<CartItem>> = dao.observeCart(userId).map { it.map { e -> e.toDomain() } }

    override suspend fun refreshCart(userId: String): Result<Unit> = runCatching { dao.upsertAll(api.getCart(userId).map { it.toEntity() }) }
}
