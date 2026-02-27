package com.deyvieat.shoptogether.features.rooms.data.datasources.local.dao

import androidx.room.*
import com.deyvieat.shoptogether.features.rooms.data.datasources.local.entities.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE isActive = 1 ORDER BY createdAt DESC")
    fun observeActiveRooms(): Flow<List<RoomEntity>>

    @Upsert suspend fun upsertAll(rooms: List<RoomEntity>)

    @Query("UPDATE rooms SET isActive = 0 WHERE id = :roomId")
    suspend fun closeRoom(roomId: String)
}
