package com.deyvieat.shoptogether.features.votes.domain.usecases

import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import javax.inject.Inject

class ConnectToRoomUseCase @Inject constructor(
    private val repository: VotesRepository
) {
    operator fun invoke(roomId: String) = repository.connectToRoom(roomId)
}
