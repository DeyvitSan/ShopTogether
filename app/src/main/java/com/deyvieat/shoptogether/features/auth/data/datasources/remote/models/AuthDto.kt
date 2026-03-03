package com.deyvieat.shoptogether.features.auth.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginRequesDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class AuthResponseDto(
    @SerializedName("user") val user: UserDto,
    @SerializedName("token") val token: String? = null
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)

data class UpdateUserRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)
