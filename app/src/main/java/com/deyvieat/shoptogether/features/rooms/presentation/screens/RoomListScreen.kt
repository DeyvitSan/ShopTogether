package com.deyvieat.shoptogether.features.rooms.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvieat.shoptogether.features.rooms.presentation.viewmodels.RoomListViewModel
import com.deyvieat.shoptogether.features.rooms.presentation.viewmodels.RoomListEvent
import com.deyvieat.shoptogether.features.rooms.presentation.components.RoomCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    onRoomClick: (String, String) -> Unit, 
    onCartClick: () -> Unit, 
    vm: RoomListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var newRoomName by remember { mutableStateOf("") }
    var newRoomDesc by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.events.collectLatest { event ->
            when (event) {
                is RoomListEvent.Error -> snackbar.showSnackbar(event.message)
                is RoomListEvent.RoomCreated -> {
                    vm.joinRoom(event.roomId)
                    onRoomClick(event.roomId, event.roomName)
                }
            }
        }
    }

    if (uiState.showCreateDialog) {
        AlertDialog(
            onDismissRequest = { vm.dismissDialog() },
            title = { Text("Nueva sala de subasta") },
            text = {
                Column {
                    OutlinedTextField(newRoomName, { newRoomName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(newRoomDesc, { newRoomDesc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = { 
                TextButton(onClick = { 
                    vm.createRoom(newRoomName, newRoomDesc)
                    newRoomName = ""
                    newRoomDesc = ""
                }) { Text("Crear") } 
            },
            dismissButton = { TextButton(onClick = { vm.dismissDialog() }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { 
            TopAppBar(
                title = { Text("Subastas ") },
                actions = {
                    IconButton(onClick = onCartClick) { Icon(Icons.Default.ShoppingCart, "Mi carrito") }
                    IconButton(onClick = { vm.refresh() }) { Icon(Icons.Default.Refresh, "Refrescar") }
                }
            ) 
        },
        floatingActionButton = { 
            FloatingActionButton(onClick = { vm.showCreateDialog() }) { 
                Icon(Icons.Default.Add, "Crear sala") 
            } 
        }
    ) { pad ->
        Box(Modifier.fillMaxSize().padding(pad)) {
            when {
                uiState.isLoading && uiState.rooms.isEmpty() -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.rooms.isEmpty() -> Text("No hay salas activas", Modifier.align(Alignment.Center))
                else -> LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
                    items(uiState.rooms, key = { it.id }) { room ->
                        RoomCard(room = room, onClick = { vm.joinRoom(room.id); onRoomClick(room.id, room.name) })
                    }
                }
            }
        }
    }
}
