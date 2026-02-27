package com.deyvieat.shoptogether.features.cart.domain.entities

data class CartItem(val id: String,
                    val userId: String,
                    val productId: String,
                    val productName: String,
                    val price: Double,
                    val imageUrl: String,
                    val quantity: Int,
                    val createdAt: String)
