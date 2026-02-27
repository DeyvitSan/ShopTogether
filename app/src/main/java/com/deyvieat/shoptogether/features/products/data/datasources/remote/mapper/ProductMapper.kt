package com.deyvieat.shoptogether.features.products.data.datasources.remote.mapper

import com.deyvieat.shoptogether.features.products.data.datasources.local.entities.ProductEntity
import com.deyvieat.shoptogether.features.products.data.datasources.remote.models.ProductDto
import com.deyvieat.shoptogether.features.products.domain.entities.Product

fun ProductDto.toEntity() = ProductEntity(id, name, price, stock, roomId, imageUrl, createdAt)
fun ProductEntity.toDomain() = Product(id, name, price, stock, roomId, imageUrl, createdAt)
