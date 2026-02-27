package com.deyvieat.shoptogether.features.cart.data.datasources.remote.api

import com.deyvieat.shoptogether.features.cart.data.datasources.remote.models.CartItemDto
import retrofit2.http.*

interface CartApi {
    @GET("users/{userId}/cart") suspend fun getCart(@Path("userId") userId: String): List<CartItemDto>
}