package com.deyvieat.shoptogether.features.auth.data.datasources.remote.api
import com.deyvieat.shoptogether.features.auth.data.datasources.remote.models.*

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/** Segun como vayan a venir los Endpoints del back*/

interface AuthApi {

    @POST("users/register")suspend fun register(@Body req: RegisterRequestDto) : AuthResponseDto

    @POST("users/login")suspend fun login(@Body req: LoginRequestDto) : AuthResponseDto

    @PUT("users/{id}") suspend fun updateUser(@Path("id") id: String, @Body req: UpdateUserRequestDto) UserDto

    @DELETE("users/{id}") suspend fun deleteUser(@Path("id") id: String)
}