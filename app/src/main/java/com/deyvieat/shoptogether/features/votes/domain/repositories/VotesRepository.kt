package com.deyvieat.shoptogether.features.votes.domain.repositories

import com.deyvieat.shoptogether.features.votes.data.datasources.remote.models.IncomingVoteDto
import com.deyvieat.shoptogether.features.votes.domain.entities.Vote
import kotlinx.coroutines.flow.Flow

interface VotesRepository {
    fun observeVotes(productId: String): Flow<List<Vote>>
    suspend fun refreshVotes(productId: String): Result<Unit>
    fun connectToRoom(roomId: String): Flow<IncomingVoteDto>
    suspend fun placeVote(roomId: String, productId: String, userId: String, value: Double): Result<Unit>
    suspend fun persistIncomingVote(dto: IncomingVoteDto)
    suspend fun confirmVote(id: String)
    suspend fun failVote(id: String)
}
