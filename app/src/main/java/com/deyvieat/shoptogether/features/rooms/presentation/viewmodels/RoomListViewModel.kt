package com.deyvieat.shoptogether.features.rooms.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom
import com.deyvieat.shoptogether.features.rooms.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomListUiState(
    val isLoading: Boolean = false, 
    val rooms: List<AuctionRoom> = emptyList(), 
    val showCreateDialog: Boolean = false
)

sealed class RoomListEvent {
    data class Error(val message: String) : RoomListEvent()
    data class RoomCreated(val roomId: String, val roomName: String) : RoomListEvent()
}

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val getRoomsUseCase: GetRoomsUseCase,
    private val refreshRoomsUseCase: RefreshRoomsUseCase,
    private val createRoomUseCase: CreateRoomUseCase,
    private val joinRoomUseCase: JoinRoomUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoomListUiState())
    val uiState: StateFlow<RoomListUiState> = _uiState.asStateFlow()
    
    private val _events = MutableSharedFlow<RoomListEvent>()
    val events: SharedFlow<RoomListEvent> = _events.asSharedFlow()

    init {
        getRoomsUseCase().onEach { rooms -> 
            _uiState.update { it.copy(rooms = rooms, isLoading = false) } 
        }.launchIn(viewModelScope)
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        refreshRoomsUseCase().onFailure { 
            _events.emit(RoomListEvent.Error("Error al cargar salas: ${it.message}")) 
        }
    }

    fun createRoom(name: String, description: String) = viewModelScope.launch {
        _uiState.update { it.copy(showCreateDialog = false) }
        createRoomUseCase(name, description).fold(
            onSuccess = { room ->
                _events.emit(RoomListEvent.RoomCreated(room.id, room.name))
            },
            onFailure = { 
                _events.emit(RoomListEvent.Error("Error al crear sala: ${it.message}")) 
            }
        )
    }

    fun joinRoom(roomId: String) = viewModelScope.launch {
        joinRoomUseCase(roomId).onFailure { 
            _events.emit(RoomListEvent.Error("Error al unirse: ${it.message}")) 
        }
    }

    fun showCreateDialog() = _uiState.update { it.copy(showCreateDialog = true) }
    fun dismissDialog()    = _uiState.update { it.copy(showCreateDialog = false) }
}
