package com.deyvieat.shoptogether.features.cart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn;
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment; import androidx.compose.ui.Modifier; import androidx.compose.ui.text.font.FontWeight; import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvieat.shoptogether.features.cart.presentation.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(onBack: () -> Unit, vm: CartViewModel = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Mi carrito ") },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack,
            null) } }) }) { pad ->

        Box(Modifier.fillMaxSize().padding(pad)) {

            if (uiState.isLoading) CircularProgressIndicator(Modifier.align(Alignment.Center))

            else if (uiState.items.isEmpty()) Text("Tu carrito está vacío",
                Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            else LazyColumn(Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)) {

                item {
                    Text("Productos ganados en subasta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(12.dp))
                }

                items(uiState.items,
                    key = { it.id }) { item ->

                    Card(Modifier.fillMaxWidth().
                    padding(vertical = 4.dp)) {

                        Row(Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {

                            Column(Modifier.weight(1f)) {
                                Text(item.productName, fontWeight = FontWeight.Bold)
                                Text("Cantidad: ${item.quantity}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("$${item.price * item.quantity}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(12.dp))
                    val total = uiState.items.sumOf { it.price * it.quantity }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total:", style = MaterialTheme.typography.titleLarge)
                        Text("$$total", style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
