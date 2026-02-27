package com.deyvieat.shoptogether.features.cart.data.datasources.remote.mapper

import com.deyvieat.shoptogether.features.cart.data.datasources.local.entities.CartEntity
import com.deyvieat.shoptogether.features.cart.data.datasources.remote.models.CartItemDto
import com.deyvieat.shoptogether.features.cart.domain.entities.CartItem

fun CartItemDto.toEntity() = CartEntity(id, userId, productId, productName, price, imageUrl, quantity, createdAt)
fun CartEntity.toDomain() = CartItem(id, userId, productId, productName, productPrice, productImage, quantity, createdAt)
