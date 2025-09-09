package ru.mokolomyagi.photofactcheck.ui.workorders

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun WorkOrderDetailsScreen(
    orderId: String,
    viewModel: WorkOrderDetailsViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) { viewModel.loadDetails(orderId) }

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.error ?: "Ошибка", color = MaterialTheme.colorScheme.error) }
        else -> {
            val order = state.order ?: return
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text(order.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("Статус: ${order.status}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("Назначено: ${order.assignedTo}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(order.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))

                order.location?.let { Text("Геопозиция: ${it.lat}, ${it.lon}") ; Spacer(Modifier.height(8.dp)) }

                if (order.materials.isNotEmpty()) {
                    Text("Расходные материалы:", style = MaterialTheme.typography.titleMedium)
                    order.materials.forEach { Text("- $it") }
                    Spacer(Modifier.height(12.dp))
                }

                Text("Фото до:", style = MaterialTheme.typography.titleMedium)
                PhotoRow(order.photosBefore) { viewModel.addPhotoBefore(it) }

                Spacer(Modifier.height(8.dp))

                Text("Фото после:", style = MaterialTheme.typography.titleMedium)
                PhotoRow(order.photosAfter) { viewModel.addPhotoAfter(it) }
            }
        }
    }
}

@Composable
fun PhotoRow(photos: List<String>, onAddPhoto: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        items(photos) {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(80.dp).clickable { /* открыть полноразмер */ }
            )
        }
        item { Button(onClick = { onAddPhoto("fake_uri_${photos.size + 1}") }) { Text("+") } }
    }
}
