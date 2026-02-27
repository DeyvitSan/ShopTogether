package com.deyvieat.shoptogether.features.rooms.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment; import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight; import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvieat.shoptogether.features.rooms.presentation.viewmodels.AuctionRoomViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AuctionRoomScreen(roomId: String, roomName: String, onBack: () -> Unit, vm: AuctionRoomViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { vm.events.collectLatest { snackbar.showSnackbar(it) } }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Column { Text(roomName, style = MaterialTheme.typography.titleMedium); Text(if (uiState.isConnected) "🟢 En vivo" else "🔴 Desconectado", style = MaterialTheme.typography.labelSmall) } },
                navigationIcon = { IconButton(onClick = { vm.leave(); onBack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = { IconButton(onClick = { vm.close(); onBack() }) { Icon(Icons.Default.Close, "Cerrar sala") } }
            )
        }
    ) { pad ->
        Row(Modifier.fillMaxSize().padding(pad)) {
            // Panel izquierdo: productos de la sala
            LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) {
                item { Text("Productos", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(8.dp)) }
                items(uiState.products, key = { it.id }) { product ->
                    val isSelected = uiState.selectedProduct?.id == product.id
                    Card(Modifier.fillMaxWidth().padding(4.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                    ) {
                        Column(Modifier.padding(12.dp).fillMaxWidth()) {
                            TextButton(onClick = { vm.selectProduct(product) }, Modifier.fillMaxWidth()) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(product.name, fontWeight = FontWeight.Bold)
                                    Text("$${product.price}", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            VerticalDivider()

            // Panel derecho: votos en tiempo real del producto seleccionado
            Column(Modifier.weight(1f).padding(8.dp)) {
                uiState.selectedProduct?.let { product ->
                    Text(product.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    // Estrellas para votar (1-5)
                    Text("Tu voto:", style = MaterialTheme.typography.labelMedium)
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { vm.onStarSelect(star) }) {
                                Icon(if (star <= uiState.myVoteValue) Icons.Default.Star else Icons.Default.StarBorder,
                                    null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    Button(onClick = { vm.vote() }, enabled = uiState.myVoteValue > 0 && !uiState.isVoting, modifier = Modifier.fillMaxWidth()) {
                        Text("Votar ★${uiState.myVoteValue}")
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Votos en tiempo real:", style = MaterialTheme.typography.labelMedium)
                    LazyColumn(Modifier.fillMaxHeight()) {
                        items(uiState.votes, key = { it.id }) { vote ->
                            ListItem(headlineContent = { Text("★".repeat(vote.value)) },
                                supportingContent = { if (vote.isPending) Text("enviando...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                trailingContent = { Text(vote.userId.take(6)) })
                        }
                    }
                } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("← Selecciona un producto", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
