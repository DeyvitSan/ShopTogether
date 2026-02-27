package com.deyvieat.shoptogether.features.products.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class ProductDto(@SerializedName("id") val id: String,
                      @SerializedName("name") val name: String,
                      @SerializedName("price") val price: Double,
                      @SerializedName("stock") val stock: Int,
                      @SerializedName("room_id") val roomId: String,
                      @SerializedName("image_url") val imageUrl: String = "",
                      @SerializedName("created_at") val createdAt: String)

data class CreateProductRequestDto(@SerializedName("name") val name: String,
                                   @SerializedName("price") val price: Double,
                                   @SerializedName("stock") val stock: Int,
                                   @SerializedName("image_url") val imageUrl: String = "")
