package com.deyvieat.shoptogether.features.rooms.domain.usecases

import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository; import javax.inject.Inject
class JoinRoomUseCase @Inject constructor(private val repo: RoomsRepository) {
    suspend operator fun invoke(roomId: String) = repo.joinRoom(roomId)
}
