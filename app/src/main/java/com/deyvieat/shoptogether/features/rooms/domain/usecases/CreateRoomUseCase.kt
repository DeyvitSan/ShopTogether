package com.deyvieat.shoptogether.features.rooms.domain.usecases

import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository; import javax.inject.Inject
class CreateRoomUseCase @Inject constructor(private val repo: RoomsRepository) {
    suspend operator fun invoke(name: String, description: String) = repo.createRoom(name, description)
}
