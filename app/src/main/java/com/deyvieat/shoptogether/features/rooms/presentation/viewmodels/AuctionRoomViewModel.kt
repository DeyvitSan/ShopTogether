package com.deyvieat.shoptogether.features.rooms.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvieat.shoptogether.core.session.SessionManager
import com.deyvieat.shoptogether.features.products.domain.entities.Product
import com.deyvieat.shoptogether.features.products.domain.usecases.CreateProductUseCase
import com.deyvieat.shoptogether.features.products.domain.usecases.GetProductsUseCase
import com.deyvieat.shoptogether.features.products.domain.usecases.RefreshProductsUseCase
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import com.deyvieat.shoptogether.features.rooms.domain.usecases.*
import com.deyvieat.shoptogether.features.votes.data.datasources.remote.models.IncomingVoteDto
import com.deyvieat.shoptogether.features.votes.domain.usecases.ConnectToRoomUseCase
import com.deyvieat.shoptogether.features.votes.domain.usecases.ObserveVotesUseCase
import com.deyvieat.shoptogether.features.votes.domain.usecases.PlaceVoteUseCase
import com.deyvieat.shoptogether.features.votes.domain.repositories.VotesRepository
import com.deyvieat.shoptogether.features.votes.domain.entities.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuctionRoomUiState(
    val isConnected: Boolean = false,
    val room: AuctionRoom? = null,
    val products: List<Product> = emptyList(),
    val votes: List<Vote> = emptyList(),
    val selectedProduct: Product? = null,
    val currentUserId: String = "",
    val myBidAmount: String = "",
    val isVoting: Boolean = false,
    val showCreateProductDialog: Boolean = false,
    val isCreatingProduct: Boolean = false
) {
    val isOwner: Boolean = room?.createdBy == currentUserId && currentUserId.isNotEmpty()
    val currentHighestBid: Double = (votes.maxByOrNull { it.value }?.value ?: selectedProduct?.price ?: 0.0)
}

@HiltViewModel
class AuctionRoomViewModel @Inject constructor(
    private val connectToRoom: ConnectToRoomUseCase,
    private val observeVotes: ObserveVotesUseCase,
    private val placeVote: PlaceVoteUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    private val getProducts: GetProductsUseCase,
    private val refreshProducts: RefreshProductsUseCase,
    private val createProductUseCase: CreateProductUseCase,
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

    private var votesJob: Job? = null

    init {
        viewModelScope.launch {
            session.userId.collect { id -> 
                _uiState.update { it.copy(currentUserId = id ?: "") } 
            }
        }
        
        getRoomUseCase(roomId).onEach { room ->
            _uiState.update { it.copy(room = room) }
        }.launchIn(viewModelScope)

        getProducts(roomId).onEach { products -> 
            _uiState.update { it.copy(products = products) } 
        }.launchIn(viewModelScope)
        
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
        val selectedProductId = _uiState.value.selectedProduct?.id
        val fixedDto = if (dto.productId.isEmpty() && selectedProductId != null) {
            dto.copy(productId = selectedProductId)
        } else dto

        votesRepo.persistIncomingVote(fixedDto)
    }

    fun selectProduct(product: Product) {
        _uiState.update { it.copy(selectedProduct = product, myBidAmount = "") }
        
        // Cancelamos la observación anterior y empezamos una nueva para el producto seleccionado
        votesJob?.cancel()
        votesJob = observeVotes(product.id).onEach { votes -> 
            _uiState.update { it.copy(votes = votes) } 
        }.launchIn(viewModelScope)

        // Cargamos las ofertas existentes desde el servidor (HTTP)
        viewModelScope.launch {
            votesRepo.refreshVotes(product.id)
        }
    }

    fun onBidAmountChange(amount: String) {
        _uiState.update { it.copy(myBidAmount = amount) }
    }

    fun vote() {
        if (_uiState.value.isOwner) return
        val productId = _uiState.value.selectedProduct?.id ?: return
        val bidValue = _uiState.value.myBidAmount.toDoubleOrNull() ?: return
        
        if (bidValue <= _uiState.value.currentHighestBid) {
            viewModelScope.launch { _events.emit("La oferta debe ser mayor al precio actual") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isVoting = true) }
            placeVote(roomId, productId, _uiState.value.currentUserId, bidValue)
                .onSuccess { _uiState.update { it.copy(myBidAmount = "") } }
                .onFailure { _events.emit("No se pudo enviar la oferta") }
            _uiState.update { it.copy(isVoting = false) }
        }
    }

    fun createProduct(name: String, price: Double, stock: Int, imageUrl: String) = viewModelScope.launch {
        if (!_uiState.value.isOwner) return@launch
        _uiState.update { it.copy(isCreatingProduct = true) }
        createProductUseCase(roomId, name, price, stock, imageUrl).fold(
            onSuccess = {
                _uiState.update { it.copy(isCreatingProduct = false, showCreateProductDialog = false) }
                _events.emit("Producto creado")
                refreshProducts(roomId)
            },
            onFailure = {
                _uiState.update { it.copy(isCreatingProduct = false) }
                _events.emit("Error al crear producto: ${it.message}")
            }
        )
    }

    fun showCreateProductDialog() {
        if (_uiState.value.isOwner) _uiState.update { it.copy(showCreateProductDialog = true) }
    }
    fun dismissCreateProductDialog() = _uiState.update { it.copy(showCreateProductDialog = false) }
    fun leave() = viewModelScope.launch { leaveRoom(roomId) }
    fun close() = viewModelScope.launch { closeRoom(roomId).onSuccess { _events.emit("Sala cerrada") } }
}
