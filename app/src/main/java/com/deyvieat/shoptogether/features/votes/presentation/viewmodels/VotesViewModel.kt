package com.deyvieat.shoptogether.features.votes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvieat.shoptogether.core.session.SessionManager
import com.deyvieat.shoptogether.features.votes.domain.entities.Vote
import com.deyvieat.shoptogether.features.votes.domain.usecases.ConnectToRoomUseCase
import com.deyvieat.shoptogether.features.votes.domain.usecases.ObserveVotesUseCase
import com.deyvieat.shoptogether.features.votes.domain.usecases.PlaceVoteUseCase
import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VotesUiState(
    val votes: List<Vote> = emptyList(),
    val isConnected: Boolean = false,
    val userId: String = ""
)

@HiltViewModel
class VotesViewModel @Inject constructor(
    private val observeVotes: ObserveVotesUseCase,
    private val connectToRoom: ConnectToRoomUseCase,
    private val placeVote: PlaceVoteUseCase,
    private val repository: VotesRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(VotesUiState())
    val uiState: StateFlow<VotesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            session.userId.collect { id ->
                _uiState.update { it.copy(userId = id ?: "") }
            }
        }
    }

    fun start(roomId: String, productId: String) {
        viewModelScope.launch {
            observeVotes(productId).collect { votes ->
                _uiState.update { it.copy(votes = votes) }
            }
        }

        viewModelScope.launch {
            connectToRoom(roomId).collect { dto ->
                repository.persistIncomingVote(dto)
            }
        }
    }

    fun vote(roomId: String, productId: String, value: Double) {
        val currentUserId = _uiState.value.userId
        if (currentUserId.isEmpty()) return

        viewModelScope.launch {
            placeVote(roomId, productId, currentUserId, value)
        }
    }
}
