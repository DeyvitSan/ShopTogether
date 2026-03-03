package com.deyvieat.shoptogether.features.votes.data.repositories

import android.util.Log
import com.deyvieat.shoptogether.core.session.SessionManager
import com.deyvieat.shoptogether.features.rooms.data.websocket.AuctionSocketManager
import com.deyvieat.shoptogether.features.votes.data.datasources.local.dao.VoteDao
import com.deyvieat.shoptogether.features.votes.data.datasources.local.entities.VoteEntity
import com.deyvieat.shoptogether.features.votes.data.datasources.remote.api.VotesApi
import com.deyvieat.shoptogether.features.votes.data.datasources.remote.models.IncomingVoteDto
import com.deyvieat.shoptogether.features.votes.domain.entities.Vote
import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import javax.inject.Inject

class VotesRepositoryImpl @Inject constructor(
    private val voteDao: VoteDao,
    private val votesApi: VotesApi, // Agregado para cargar existentes
    private val socketManager: AuctionSocketManager,
    private val session: SessionManager
) : VotesRepository {

    override fun observeVotes(productId: String): Flow<List<Vote>> {
        return voteDao.observeVotes(productId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshVotes(productId: String): Result<Unit> = runCatching {
        val response = votesApi.getByProduct(productId)
        response.forEach { dto ->
            voteDao.insert(VoteEntity(
                id = dto.id,
                userId = dto.userId,
                productId = dto.productId,
                value = dto.value
            ))
        }
    }

    override fun connectToRoom(roomId: String): Flow<IncomingVoteDto> = callbackFlow {
        val userId = session.userId.first() ?: ""
        socketManager.connect(
            roomId = roomId,
            userId = userId,
            onMessage = { type, data ->
                try {
                    val json = JSONObject(data)
                    when (type) {
                        "new_vote" -> {
                            val dto = IncomingVoteDto(
                                type = "NEW_VOTE",
                                id = json.optString("id", java.util.UUID.randomUUID().toString()),
                                userId = json.optString("userId", "Desconocido"),
                                productId = json.optString("productId", ""),
                                value = json.optDouble("value", 0.0)
                            )
                            trySend(dto)
                        }
                        "new_bid" -> {
                            val dto = IncomingVoteDto(
                                type = "NEW_BID",
                                id = java.util.UUID.randomUUID().toString(),
                                userId = json.optString("lastBidderId", "Sistema"),
                                productId = json.optString("productId", ""),
                                value = json.optDouble("currentPrice", 0.0)
                            )
                            trySend(dto)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("VOTES", "Error parsing socket message: ${e.message}")
                }
            }
        )
        awaitClose { socketManager.disconnect() }
    }

    override suspend fun placeVote(roomId: String, productId: String, userId: String, value: Double): Result<Unit> {
        return runCatching {
            socketManager.sendVote(roomId, productId, userId, value)
            Unit
        }
    }

    override suspend fun persistIncomingVote(dto: IncomingVoteDto) {
        voteDao.insert(
            VoteEntity(
                id = dto.id.ifEmpty { java.util.UUID.randomUUID().toString() },
                userId = dto.userId,
                productId = dto.productId,
                value = dto.value,
                isPending = false
            )
        )
    }

    override suspend fun confirmVote(id: String) {}
    override suspend fun failVote(id: String) { voteDao.delete(id) }

    private fun VoteEntity.toDomain() = Vote(id, userId, productId, value, isPending)
}
