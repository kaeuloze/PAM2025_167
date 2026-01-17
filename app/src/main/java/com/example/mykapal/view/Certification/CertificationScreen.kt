package com.example.mykapal.view.Certification

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.mykapal.viewmodel.CertificationViewModel
import kotlinx.coroutines.delay

@Composable
fun CertificationScreen(
    viewModel: CertificationViewModel = viewModel()
) {
    val list by viewModel.list.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val showDialog = viewModel.showDialog
    val selected = viewModel.selected

    // Load awal
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    // Search lokal + debounce
    var localSearchQuery by remember { mutableStateOf("") }
    LaunchedEffect(localSearchQuery) {
        delay(300)
    }

    // Filter lokal (tanpa ubah ViewModel)
    val filtered = remember(list, localSearchQuery) {
        if (localSearchQuery.isBlank()) list
        else {
            val q = localSearchQuery.trim().lowercase()
            list.filter {
                it.namaSertifikasi.lowercase().contains(q) ||
                        (it.deskripsi?.lowercase()?.contains(q) == true)
            }
        }
    }

    // Snackbar error
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            // kalau kamu punya clearError(), panggil di sini
            // viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isLoading) {
                FloatingActionButton(
                    onClick = {
                        viewModel.selected = null
                        viewModel.showDialog = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Sertifikasi")
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
                            "Sertifikasi",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${filtered.size} data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Search bar
                    OutlinedTextField(
                        value = localSearchQuery,
                        onValueChange = { localSearchQuery = it },
                        label = { Text("Cari nama sertifikasi / deskripsi...") },
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

                    // List / empty state
                    if (filtered.isEmpty()) {
                        CertificationEmptyState(
                            modifier = Modifier.fillMaxSize(),
                            text = if (localSearchQuery.isNotBlank()) {
                                "Tidak ditemukan sertifikasi dengan pencarian '$localSearchQuery'"
                            } else {
                                "Belum ada data sertifikasi"
                            }
                        )
                    } else {
                        LazyColumn {
                            items(filtered) { item ->
                                CertificationItem(
                                    data = item,
                                    onEdit = {
                                        viewModel.selected = item
                                        viewModel.showDialog = true
                                    },
                                    onDelete = { viewModel.delete(item.sertif_id) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog form
    if (showDialog) {
        CertificationFormDialog(
            data = selected,
            onDismiss = { viewModel.showDialog = false },
            onSave = { viewModel.save(it) }
        )
    }
}

@Composable
fun CertificationEmptyState(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Badge,
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
