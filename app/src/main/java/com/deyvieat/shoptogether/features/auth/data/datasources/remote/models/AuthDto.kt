package com.deyvieat.shoptogether.features.auth.data.datasources.remote.models

import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonToken

data class RegisterRequestDto(

    @SerializedName("name")  val name: String,

    @SerializedName("email")  val email: String,

    @SerializedName("password") val password: String
)

data class LoginRequesDto(

    @SerializedName("email") val email: String,

    @SerializedName("password") val password: String
)

data class AuthResponseDto(

    @SerializedName("token") val token: String,

    @SerializedName("user") val user: UserDto
)

data class UserDto(

    @SerializedName("id")    val id: String,

    @SerializedName("name")  val name: String,

    @SerializedName("email") val email: String
)

data class UpdateUserRequestDto(

    @SerializedName ("name")  val name: String,

    @SerializedName("email") val email: String
)