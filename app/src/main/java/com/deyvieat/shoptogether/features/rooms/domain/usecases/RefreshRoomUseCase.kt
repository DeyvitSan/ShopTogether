package com.deyvieat.shoptogether.features.rooms.domain.usecases

import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository; import javax.inject.Inject
class RefreshRoomsUseCase @Inject constructor(private val repo: RoomsRepository) {
    suspend operator fun invoke() = repo.refreshRooms()
}
