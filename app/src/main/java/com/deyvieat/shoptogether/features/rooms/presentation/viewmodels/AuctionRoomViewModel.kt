package com.deyvieat.shoptogether.features.rooms.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle; import androidx.lifecycle.ViewModel; import androidx.lifecycle.viewModelScope
import com.deyvieat.shoptogether.core.di.SessionManager
import com.deyvieat.shoptogether.features.products.domain.entities.Product
import com.deyvieat.shoptogether.features.products.domain.usecases.GetProductsUseCase
import com.deyvieat.shoptogether.features.products.domain.usecases.RefreshProductsUseCase
import com.deyvieat.shoptogether.features.rooms.domain.usecases.CloseRoomUseCase
import com.deyvieat.shoptogether.features.rooms.domain.usecases.LeaveRoomUseCase
import com.deyvieat.shoptogether.features.votes.data.datasources.remote.models.IncomingVoteDto
import com.deyvieat.shoptogether.features.votes.domain.usecases.ConnectToRoomUseCase
import com.deyvieat.shoptogether.features.votes.domain.usecases.ObserveVotesUseCase
import com.deyvieat.shoptogether.features.votes.domain.usecases.PlaceVoteUseCase
import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import com.deyvieat.shoptogether.features.votes.domain.entities.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*; import kotlinx.coroutines.launch; import javax.inject.Inject

data class AuctionRoomUiState(
    val isConnected: Boolean = false,
    val products: List<Product> = emptyList(),
    val votes: List<Vote> = emptyList(),
    val selectedProduct: Product? = null,
    val myVoteValue: Int = 0,           // 1-5 estrellas
    val isVoting: Boolean = false
)

@HiltViewModel
class AuctionRoomViewModel @Inject constructor(
    private val connectToRoom: ConnectToRoomUseCase,
    private val observeVotes: ObserveVotesUseCase,
    private val placeVote: PlaceVoteUseCase,
    private val getProducts: GetProductsUseCase,
    private val refreshProducts: RefreshProductsUseCase,
    private val leaveRoom: LeaveRoomUseCase,
    private val closeRoom: CloseRoomUseCase,
    private val votesRepo: VotesRepository,
    private val session: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val roomId: String = checkNotNull(savedStateHandle["roomId"])
    private val _uiState = MutableStateFlow(AuctionRoomUiState())
    val uiState: StateFlow<AuctionRoomUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    private var currentUserId: String = ""

    init {
        viewModelScope.launch {
            session.userId.collect { currentUserId = it ?: "" }
        }
        getProducts(roomId).onEach { products -> _uiState.update { it.copy(products = products) } }.launchIn(viewModelScope)
        viewModelScope.launch { refreshProducts(roomId) }
        connectWebSocket()
    }

    private fun connectWebSocket() = viewModelScope.launch {
        try {
            connectToRoom(roomId)
                .onStart { _uiState.update { it.copy(isConnected = true) } }
                .collect { dto -> handleIncoming(dto) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isConnected = false) }
            _events.emit("Conexión perdida")
        }
    }

    private suspend fun handleIncoming(dto: IncomingVoteDto) {
        when (dto.type) {
            "NEW_VOTE", "CONFIRMED" -> { votesRepo.persistIncomingVote(dto); if (dto.type == "CONFIRMED") votesRepo.confirmVote(dto.id) }
            "ERROR" -> { votesRepo.failVote(dto.id); _events.emit("Voto rechazado: ${dto.message}") }
        }
    }

    fun selectProduct(product: Product) {
        _uiState.update { it.copy(selectedProduct = product) }
        observeVotes(product.id).onEach { votes -> _uiState.update { it.copy(votes = votes) } }.launchIn(viewModelScope)
    }

    fun onStarSelect(value: Int) = _uiState.update { it.copy(myVoteValue = value) }

    /** Actualización optimista: el voto aparece al instante, con rollback si falla */
    fun vote() {
        val productId = _uiState.value.selectedProduct?.id ?: return
        val value     = _uiState.value.myVoteValue.takeIf { it in 1..5 } ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isVoting = true, myVoteValue = 0) }
            placeVote(roomId, productId, currentUserId, value)
                .onFailure { _events.emit("No se pudo enviar el voto") }
            _uiState.update { it.copy(isVoting = false) }
        }
    }

    fun leave() = viewModelScope.launch { leaveRoom(roomId) }
    fun close() = viewModelScope.launch { closeRoom(roomId).onSuccess { _events.emit("Sala cerrada") } }
}
