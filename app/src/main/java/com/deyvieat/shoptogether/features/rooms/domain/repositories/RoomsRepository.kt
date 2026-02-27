package com.deyvieat.shoptogether.features.rooms.domain.repositories

import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import kotlinx.coroutines.flow.Flow

interface RoomsRepository {
    fun observeActiveRooms(): Flow<List<AuctionRoom>>
    suspend fun refreshRooms(): Result<Unit>
    suspend fun createRoom(name: String, description: String): Result<AuctionRoom>
    suspend fun joinRoom(roomId: String): Result<Unit>
    suspend fun leaveRoom(roomId: String): Result<Unit>
    suspend fun closeRoom(roomId: String): Result<Unit>
}
