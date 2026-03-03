package com.deyvieat.shoptogether.features.rooms.data.datasources.remote.api

import com.deyvieat.shoptogether.features.rooms.data.datasources.remote.models.*
import retrofit2.http.*

interface RoomsApi {

    @GET("rooms")
    suspend fun getRooms(): List<RoomDto>

    @POST("rooms")
    suspend fun createRoom(
        @Body body: CreateRoomRequestDto
    ): RoomDto

    @PUT("rooms/{id}/close") // AHORA COINCIDE CON TU BACKEND
    suspend fun closeRoom(
        @Path("id") id: String
    )

    @POST("rooms/{id}/join")
    suspend fun joinRoom(
        @Path("id") id: String,
        @Body body: JoinLeaveRequest
    )

    @POST("rooms/{id}/leave")
    suspend fun leaveRoom(
        @Path("id") id: String,
        @Body body: JoinLeaveRequest
    )
}
