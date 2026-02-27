package com.deyvieat.shoptogether.features.rooms.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment; import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight; import androidx.compose.ui.text.style.TextOverflow; import androidx.compose.ui.unit.dp
import com.deyvieat.shoptogether.features.rooms.domain.entities.AuctionRoom

@Composable

fun RoomCard(room: AuctionRoom, onClick: () -> Unit) {

    Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp,
        vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Column(Modifier.weight(1f)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(room.name, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f))

                    if (room.isActive) Badge(containerColor = MaterialTheme.colorScheme.error) { Text("VIVO") }
                }
                Spacer(Modifier.height(4.dp))
                Text(room.description, style = MaterialTheme.typography.bodySmall,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
