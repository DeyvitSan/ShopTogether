package com.deyvieat.shoptogether.features.rooms.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvieat.shoptogether.features.rooms.presentation.viewmodels.AuctionRoomViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionRoomScreen(
    roomId: String, 
    roomName: String, 
    onBack: () -> Unit, 
    vm: AuctionRoomViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    var pName by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pStock by remember { mutableStateOf("") }
    var pImage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.events.collectLatest { snackbar.showSnackbar(it) }
    }

    if (uiState.showCreateProductDialog) {
        AlertDialog(
            onDismissRequest = { vm.dismissCreateProductDialog() },
            title = { Text("Nuevo producto para subasta") },
            text = {
                Column {
                    OutlinedTextField(pName, { pName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(pPrice, { pPrice = it }, label = { Text("Precio inicial") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(pStock, { pStock = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(pImage, { pImage = it }, label = { Text("URL Imagen (opcional)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val price = pPrice.toDoubleOrNull() ?: 0.0
                        val stock = pStock.toIntOrNull() ?: 1
                        vm.createProduct(pName, price, stock, pImage)
                        pName = ""; pPrice = ""; pStock = ""; pImage = ""
                    },
                    enabled = !uiState.isCreatingProduct
                ) { Text("Crear") }
            },
            dismissButton = { TextButton(onClick = { vm.dismissCreateProductDialog() }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { 
                    Column { 
                        Text(roomName, style = MaterialTheme.typography.titleMedium)
                        Text(if (uiState.isConnected) "🟢 En vivo" else "🔴 Desconectado", style = MaterialTheme.typography.labelSmall) 
                    } 
                },
                navigationIcon = { 
                    IconButton(onClick = { vm.leave(); onBack() }) { 
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null) 
                    } 
                },
                actions = { 
                    if (uiState.isOwner) {
                        IconButton(onClick = { vm.showCreateProductDialog() }) { Icon(Icons.Default.Add, "Agregar producto") }
                        
                        // Botón de Cerrar Sala más destacado
                        Button(
                            onClick = { vm.close(); onBack() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.Close, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Finalizar", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            )
        }
    ) { pad ->
        Row(Modifier.fillMaxSize().padding(pad)) {
            // Panel izquierdo: productos
            LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) {
                item { Text("Productos", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(8.dp)) }
                items(uiState.products, key = { it.id }) { product ->
                    val isSelected = uiState.selectedProduct?.id == product.id
                    Card(
                        Modifier.fillMaxWidth().padding(4.dp),
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

            // Panel derecho: Ofertas
            Column(Modifier.weight(1.2f).padding(8.dp)) {
                uiState.selectedProduct?.let { product ->
                    Text(product.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Precio actual: $${uiState.currentHighestBid}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    if (!uiState.isOwner) {
                        OutlinedTextField(
                            value = uiState.myBidAmount,
                            onValueChange = { vm.onBidAmountChange(it) },
                            label = { Text("Tu oferta") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            prefix = { Text("$") }
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { vm.vote() }, 
                            enabled = (uiState.myBidAmount.toDoubleOrNull() ?: 0.0) > uiState.currentHighestBid && !uiState.isVoting, 
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.isVoting) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                            else Text("Ofertar")
                        }
                    } else {
                        Text("Subasta en curso (Viendo como dueño)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }

                    Spacer(Modifier.height(24.dp))
                    Text("Ofertas recientes:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (uiState.votes.isEmpty()) {
                            item {
                                Text(
                                    "No hay ofertas todavía. ¡Sé el primero!",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(uiState.votes.sortedByDescending { it.value }) { vote ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    ListItem(
                                        headlineContent = { Text("S/ ${vote.value}", fontWeight = FontWeight.Bold) },
                                        supportingContent = { Text("Usuario: ${vote.userId.take(8)}...") },
                                        trailingContent = { if (vote.isPending) Text("Enviando...") else Icon(Icons.Default.Check, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary) }
                                    )
                                }
                            }
                        }
                    }
                } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("← Selecciona un producto para ver la subasta", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
