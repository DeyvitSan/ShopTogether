package com.deyvieat.shoptogether.features.cart.data.datasources.remote.models
import com.google.gson.annotations.SerializedName
data class CartItemDto(@SerializedName("id") val id: String,
                       @SerializedName("user_id") val userId: String,
                       @SerializedName("product_id") val productId: String,
                       @SerializedName("product_name") val productName: String = "",
                       @SerializedName("price") val price: Double = 0.0,
                       @SerializedName("image_url") val imageUrl: String = "",
                       @SerializedName("quantity") val quantity: Int,
                       @SerializedName("created_at") val createdAt: String)
