package com.deyvieat.shoptogether.features.rooms.domain.usecases

import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import com.deyvieat.shoptogether.features.rooms.domain.repositories.RoomsRepository
import kotlinx.coroutines.flow.Flow; import javax.inject.Inject

class GetRoomsUseCase @Inject constructor(private val repo: RoomsRepository) {
    operator fun invoke(): Flow<List<AuctionRoom>> = repo.observeActiveRooms()
}
