package com.deyvieat.shoptogether.features.products.data.datasources.local.entities

import androidx.room.Entity; import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(@PrimaryKey val id: String, val name: String, val price: Double, val stock: Int, val roomId: String, val imageUrl: String, val createdAt: String)
