package com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

/*Segun la respuesta real del backend */

data class RoomDto(
    @SerializedName("id")          val id: String,
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("createdBy")   val createdBy: String,
    @SerializedName("isActive")    val isActive: Boolean,
    @SerializedName("createdAt")   val createdAt: String
)

data class CreateRoomRequestDto(
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("createdBy")   val createdBy: String
)


data class JoinLeaveRequest(
    @SerializedName("userId") val userId: String
)