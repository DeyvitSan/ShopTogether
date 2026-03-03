package com.deyvieat.shoptogether.features.votes.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvieat.shoptogether.features.votes.presentation.viewmodels.VotesViewModel

@Composable
fun VotesScreen(
    roomId: String,
    productId: String,
    viewModel: VotesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(roomId, productId) {
        viewModel.start(roomId, productId)
    }

    val highestBid = uiState.votes.maxByOrNull { it.value }?.value ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ofertas en tiempo real",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Oferta actual: S/ $highestBid",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = uiState.votes.sortedByDescending { it.value },
                key = { it.id }
            ) { vote ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Usuario: ${vote.userId}")
                        Text(
                            text = "Oferta: S/ ${vote.value}",
                            style = if (vote.value == highestBid)
                                MaterialTheme.typography.titleMedium
                            else
                                MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.vote(
                    roomId = roomId,
                    productId = productId,
                    value = highestBid + 5
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ofertar +5")
        }
    }
}
