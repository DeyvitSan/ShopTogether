package com.deyvieat.shoptogether.features.votes.domain.usecases

import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import javax.inject.Inject

class PlaceVoteUseCase @Inject constructor(
    private val repository: VotesRepository
) {
    suspend operator fun invoke(roomId: String, productId: String, userId: String, value: Double) =
        repository.placeVote(roomId, productId, userId, value)
}
