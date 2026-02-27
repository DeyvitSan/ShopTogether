package com.deyvieat.shoptogether.features.rooms.data.repositories

import com.deyvieat.shoptogether.features.rooms.data.datasources.local.dao.RoomDao
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.api.RoomsApi
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.mapper.toDomain
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.mapper.toEntity
import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models.CreateRoomRequestDto
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository
import kotlinx.coroutines.flow.Flow; import kotlinx.coroutines.flow.map; import javax.inject.Inject

class RoomsRepositoryImpl @Inject constructor(private val api: RoomsApi, private val dao: RoomDao) : RoomsRepository {
    override fun observeActiveRooms(): Flow<List<AuctionRoom>> = dao.observeActiveRooms().map { it.map { e -> e.toDomain() } }
    override suspend fun refreshRooms(): Result<Unit> = runCatching { dao.upsertAll(api.getRooms().map { it.toEntity() }) }
    override suspend fun createRoom(name: String, description: String): Result<AuctionRoom> = runCatching { val r = api.createRoom(CreateRoomRequestDto(name, description)); dao.upsertAll(listOf(r.toEntity())); r.toEntity().toDomain() }
    override suspend fun joinRoom(roomId: String): Result<Unit>  = runCatching { api.joinRoom(roomId) }
    override suspend fun leaveRoom(roomId: String): Result<Unit> = runCatching { api.leaveRoom(roomId) }
    override suspend fun closeRoom(roomId: String): Result<Unit> = runCatching { api.closeRoom(roomId); dao.closeRoom(roomId) }
}
