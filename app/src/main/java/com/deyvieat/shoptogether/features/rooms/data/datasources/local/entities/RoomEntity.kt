package com.deyvieat.shoptogether.features.rooms.data.datasources.local.entities

import androidx.room.Entity; import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val isActive: Boolean,
    val createdAt: String
)
