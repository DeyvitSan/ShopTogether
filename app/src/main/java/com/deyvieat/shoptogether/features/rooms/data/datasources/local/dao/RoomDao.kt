package com.deyvieat.shoptogether.features.rooms.data.datasources.local.dao

import androidx.room.*
import com.deyvieat.shoptogether.features.rooms.data.datasources.local.entities.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE isActive = 1 ORDER BY createdAt DESC")
    fun observeActiveRooms(): Flow<List<RoomEntity>>

    @Upsert 
    suspend fun upsertAll(rooms: List<RoomEntity>)

    @Query("DELETE FROM rooms WHERE id = :roomId")
    suspend fun deleteRoom(roomId: String) // Cambiado de update a delete
}
