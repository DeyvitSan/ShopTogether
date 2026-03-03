package com.deyvieat.shoptogether.features.rooms.data.repositories

import com.deyvieat.shoptogether.core.session.SessionManager
import com.deyvieat.shoptogether.features.rooms.data.datasources.local.dao.RoomDao
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.api.RoomsApi
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.mapper.toDomain
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.mapper.toEntity
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models.CreateRoomRequestDto
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models.JoinLeaveRequest
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomsRepositoryImpl @Inject constructor(
    private val api: RoomsApi,
    private val dao: RoomDao,
    private val sessionManager: SessionManager
) : RoomsRepository {

    override fun observeActiveRooms(): Flow<List<AuctionRoom>> =
        dao.observeActiveRooms().map { it.map { e -> e.toDomain() } }

    override suspend fun refreshRooms(): Result<Unit> = runCatching {
        dao.upsertAll(api.getRooms().map { it.toEntity() })
    }

    override suspend fun createRoom(name: String, description: String): Result<AuctionRoom> = runCatching {
        val userId = sessionManager.userId.first() ?: throw IllegalStateException("User not logged in")
        val r = api.createRoom(CreateRoomRequestDto(name, description, userId))
        dao.upsertAll(listOf(r.toEntity()))
        r.toEntity().toDomain()
    }

    override suspend fun joinRoom(roomId: String): Result<Unit> = runCatching {
        val userId = sessionManager.userId.first() ?: throw IllegalStateException("User not logged in")
        api.joinRoom(roomId, JoinLeaveRequest(userId))
    }

    override suspend fun leaveRoom(roomId: String): Result<Unit> = runCatching {
        val userId = sessionManager.userId.first() ?: throw IllegalStateException("User not logged in")
        api.leaveRoom(roomId, JoinLeaveRequest(userId))
    }

    override suspend fun closeRoom(roomId: String): Result<Unit> = runCatching {
        // Llamamos a la API para cerrar en el servidor
        api.closeRoom(roomId)
        // Eliminamos físicamente de la base de datos local
        dao.deleteRoom(roomId)
    }
}
