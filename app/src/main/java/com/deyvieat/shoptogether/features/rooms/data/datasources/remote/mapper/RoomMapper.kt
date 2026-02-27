package com.deyvieat.shoptogether.features.rooms.data.datasources.remote.mapper

import com.deyvieat.shoptogether.features.rooms.data.datasources.local.entities.RoomEntity
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models.RoomDto
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom

fun RoomDto.toEntity() = RoomEntity(id, name, description, createdBy, isActive, createdAt)
fun RoomEntity.toDomain() = AuctionRoom(id, name, description, createdBy, isActive, createdAt)
