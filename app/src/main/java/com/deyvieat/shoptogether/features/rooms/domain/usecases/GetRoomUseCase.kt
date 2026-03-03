package com.deyvieat.shoptogether.features.rooms.domain.usecases

import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRoomUseCase @Inject constructor(
    private val repository: RoomsRepository
) {
    operator fun invoke(roomId: String) = 
        repository.observeActiveRooms().map { it.find { r -> r.id == roomId } }
}
