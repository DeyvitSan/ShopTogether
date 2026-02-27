package com.deyvieat.shoptogether.features.cart.data.datasources.local.entities

import androidx.room.Entity; import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(@PrimaryKey val id: String, val userId: String, val productId: String, val productName: String, val productPrice: Double, val productImage: String, val quantity: Int, val createdAt: String)
