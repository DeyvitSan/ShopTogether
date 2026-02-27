package com.deyvieat.shoptogether.features.products.data.datasources.remote.api

import com.deyvieat.shoptogether.features.products.data.datasources.remote.models.*
import retrofit2.http.*

interface ProductsApi {
    @GET("rooms/{roomId}/products")  suspend fun getProducts(@Path("roomId") roomId: String): List<ProductDto>
    @POST("rooms/{roomId}/products") suspend fun createProduct(@Path("roomId") roomId: String, @Body req: CreateProductRequestDto): ProductDto
}