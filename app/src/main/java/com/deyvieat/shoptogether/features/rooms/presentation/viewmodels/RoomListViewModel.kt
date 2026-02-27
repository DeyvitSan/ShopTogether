package com.deyvieat.shoptogether.features.rooms.presentation.viewmodels

import androidx.lifecycle.ViewModel; import androidx.lifecycle.viewModelScope
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import com.deyvieat.shoptogether.features.rooms.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*; import kotlinx.coroutines.launch; import javax.inject.Inject

data class RoomListUiState(val isLoading: Boolean = false, val rooms: List<AuctionRoom> = emptyList(), val showCreateDialog: Boolean = false)

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val getRooms: GetRoomsUseCase,
    private val refreshRooms: RefreshRoomsUseCase,
    private val createRoom: CreateRoomUseCase,
    private val joinRoom: JoinRoomUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoomListUiState())
    val uiState: StateFlow<RoomListUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    init {
        getRooms().onEach { rooms -> _uiState.update { it.copy(rooms = rooms, isLoading = false) } }.launchIn(viewModelScope)
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        refreshRooms().onFailure { _events.emit("Error al cargar salas: ${it.message}") }
    }

    fun createRoom(name: String, description: String) = viewModelScope.launch {
        _uiState.update { it.copy(showCreateDialog = false) }
        createRoom(name, description).onFailure { _events.emit("Error al crear sala: ${it.message}") }
    }

    fun joinRoom(roomId: String) = viewModelScope.launch {
        joinRoom(roomId).onFailure { _events.emit("Error al unirse: ${it.message}") }
    }

    fun showCreateDialog() = _uiState.update { it.copy(showCreateDialog = true) }
    fun dismissDialog()    = _uiState.update { it.copy(showCreateDialog = false) }
}
