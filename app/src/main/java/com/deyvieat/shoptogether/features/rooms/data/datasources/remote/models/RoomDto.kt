package com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

/*Segun la respuesta real del backend */

data class RoomDto(
    @SerializedName("id")          val id: String,
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_by")  val createdBy: String,
    @SerializedName("is_active")   val isActive: Boolean,
    @SerializedName("created_at")  val createdAt: String
)

data class CreateRoomRequestDto(
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String
)
