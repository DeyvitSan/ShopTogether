package com.deyvieat.shoptogether.features.rooms.data.datasources.remote.mapper

import com.deyvieat.shoptogether.features.rooms.data.datasources.local.entities.RoomEntity
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models.RoomDto
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom

fun RoomDto.toEntity() = RoomEntity(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy,
    isActive = isActive,
    createdAt = createdAt
)

fun RoomEntity.toDomain() = AuctionRoom(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy,
    isActive = isActive,
    createdAt = createdAt
)
