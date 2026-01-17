package com.example.mykapal.view.crew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykapal.viewmodel.CrewViewModel
import kotlinx.coroutines.delay

@Composable
fun CrewScreen(
    viewModel: CrewViewModel = viewModel()
) {
    val crewList by viewModel.crewList.collectAsState()
    val filteredCrew by viewModel.filteredCrew.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showDialog = viewModel.showDialog
    val selectedCrew = viewModel.selectedCrew

    var localSearchQuery by remember { mutableStateOf("") }

    // Local state untuk search query
    // Update search query di ViewModel ketika user mengetik
    LaunchedEffect(localSearchQuery) {
        // Gunakan debounce untuk performance
        delay(300) // Tunggu 300ms setelah user berhenti mengetik
        viewModel.updateSearchQuery(localSearchQuery)
    }

    // Handle error dengan Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            // Clear error setelah ditampilkan
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isLoading) {
                FloatingActionButton(
                    onClick = { viewModel.openDialog() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Kru")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tampilkan loading
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Data Kru",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        // Tampilkan jumlah data
                        Text(
                            text = "${filteredCrew.size} kru",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Search bar
                    OutlinedTextField(
                        value = localSearchQuery,
                        onValueChange = { localSearchQuery = it },
                        label = { Text("Cari nama kru, posisi, passport...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        trailingIcon = {
                            if (localSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { localSearchQuery = "" }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Sorting options (optional)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.sortBy(CrewViewModel.SortField.NAME) },
                            label = { Text("Nama") }
                        )
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.sortBy(CrewViewModel.SortField.POSITION) },
                            label = { Text("Posisi") }
                        )
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.sortBy(CrewViewModel.SortField.AGE) },
                            label = { Text("Umur") }
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Crew list
                    if (filteredCrew.isEmpty()) {
                        EmptyState(
                            modifier = Modifier.fillMaxSize(),
                            text = if (localSearchQuery.isNotEmpty()) {
                                "Tidak ditemukan kru dengan pencarian '$localSearchQuery'"
                            } else {
                                "Belum ada data kru"
                            }
                        )
                    } else {
                        LazyColumn {
                            items(filteredCrew) { crew ->
                                CrewItem(
                                    crew = crew,
                                    onEdit = { viewModel.openDialog(crew) },
                                    onDelete = { viewModel.deleteCrew(crew.crew_id) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog untuk form
    if (showDialog) {
        CrewFormDialog(
            crew = selectedCrew,
            onDismiss = { viewModel.closeDialog() },
            onSave = { viewModel.saveCrew(it) }
        )
    }
}

// Empty state component
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.PersonOff,
            contentDescription = "No data",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}