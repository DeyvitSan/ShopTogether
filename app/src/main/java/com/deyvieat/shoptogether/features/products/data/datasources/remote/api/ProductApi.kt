package com.deyvieat.shoptogether.features.products.data.datasources.remote.api

import com.deyvieat.shoptogether.features.products.data.datasources.remote.models.*
import retrofit2.http.*

interface ProductsApi {

    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @POST("products")
    suspend fun createProduct(
        @Body req: CreateProductRequestDto
    ): ProductDto

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Body req: CreateProductRequestDto
    ): ProductDto

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String
    )
}