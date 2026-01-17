package com.example.mykapal.view.CrewCertification

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
import com.example.mykapal.viewmodel.CrewCertificationViewModel
import kotlinx.coroutines.delay

@Composable
fun CrewCertificationScreen(
    viewModel: CrewCertificationViewModel = viewModel()
) {
    val list by viewModel.list.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // dialog state (kalau di ViewModel kamu sudah flow)
    val showDialog by viewModel.showDialog.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()

    // dialog dropdown data
    val certList by viewModel.certList.collectAsState()
    val crewList by viewModel.crewList.collectAsState()

    // Local search
    var localSearchQuery by remember { mutableStateOf("") }

    // Debounce search
    LaunchedEffect(localSearchQuery) {
        delay(300)
        // kalau ViewModel kamu belum punya ini, tinggal bikin fungsi updateSearchQuery()
        viewModel.updateSearchQuery(localSearchQuery)
    }

    // Load pertama kali
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    // Snackbar error
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    // filtered list (kalau ViewModel belum punya filteredList, ini fallback local)
    val filteredList = remember(list, localSearchQuery) {
        if (localSearchQuery.isBlank()) list
        else {
            val q = localSearchQuery.trim()
            list.filter { item ->
                item.nama_sertifikasi.contains(q, ignoreCase = true) ||
                        item.kru.contains(q, ignoreCase = true) ||
                        item.status.contains(q, ignoreCase = true) ||
                        item.tanggal_terbit.contains(q, ignoreCase = true) ||
                        item.tanggal_kadaluarsa.contains(q, ignoreCase = true)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isLoading) {
                FloatingActionButton(onClick = { viewModel.openDialog() }) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Sertifikasi Kru")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                            "Crew Certification",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${filteredList.size} data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Search bar
                    OutlinedTextField(
                        value = localSearchQuery,
                        onValueChange = { localSearchQuery = it },
                        label = { Text("Cari kru, sertifikasi, status...") },
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

                    // Optional filter chip (contoh)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.sortBy(CrewCertificationViewModel.SortField.KRU) },
                            label = { Text("Kru") }
                        )
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.sortBy(CrewCertificationViewModel.SortField.SERTIFIKASI) },
                            label = { Text("Sertifikasi") }
                        )
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.sortBy(CrewCertificationViewModel.SortField.EXPIRY) },
                            label = { Text("Kadaluarsa") }
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // List / Empty
                    if (filteredList.isEmpty()) {
                        EmptyState(
                            modifier = Modifier.fillMaxSize(),
                            text = if (localSearchQuery.isNotEmpty()) {
                                "Tidak ditemukan data dengan pencarian '$localSearchQuery'"
                            } else {
                                "Belum ada data crew certification"
                            }
                        )
                    } else {
                        LazyColumn {
                            items(filteredList) { item ->
                                CrewCertificationItem(
                                    data = item,
                                    onEdit = { viewModel.openDialog(item) },
                                    onDelete = { viewModel.delete(item.crew_cert_id) }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog form
    if (showDialog) {
        CrewCertificationFormDialog(
            data = selectedItem,
            certList = certList,
            crewList = crewList,
            onDismiss = { viewModel.closeDialog() },
            onSave = { viewModel.save(it) }
        )
    }
}

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
