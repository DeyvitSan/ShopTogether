package com.deyvieat.shoptogether.features.products.data.datasources.remote.models

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val roomId: String,
    val imageUrl: String = "",
    val createdAt: String
)

data class CreateProductRequestDto(
    val name: String,
    val price: Double,
    val stock: Int,
    val roomId: String,
    val imageUrl: String = ""
)