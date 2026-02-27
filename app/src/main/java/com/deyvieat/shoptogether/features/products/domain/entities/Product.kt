package com.deyvieat.shoptogether.features.products.domain.entities

data class Product(val id: String, val name: String, val price: Double, val stock: Int, val roomId: String, val imageUrl: String, val createdAt: String)
