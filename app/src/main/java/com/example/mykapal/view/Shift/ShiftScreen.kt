package com.example.mykapal.view.Shift

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkOff
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
import com.example.mykapal.data.model.Shift
import com.example.mykapal.viewmodel.ShiftViewModel
import kotlinx.coroutines.delay

@Composable
fun ShiftScreen(
    viewModel: ShiftViewModel = viewModel()
) {
    val shiftList by viewModel.shiftList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Pastikan ViewModel kamu punya:
    // - val showDialog: Boolean
    // - val selectedShift: Shift?
    // - fun openAddDialog()
    // - fun openEditDialog(shift: Shift)
    // - fun closeDialog()
    // - fun saveShift(shift: Shift)
    // - fun deleteShift(id: Int)
    val showDialog = viewModel.showDialog
    val selectedShift = viewModel.selectedShift

    // Search local
    var localSearchQuery by remember { mutableStateOf("") }
    var filteredList by remember { mutableStateOf(listOf<Shift>()) }

    // Load data pertama kali
    LaunchedEffect(Unit) {
        viewModel.loadShifts()
    }

    // Debounce + filter di UI (tanpa perlu tambah fungsi di ViewModel)
    LaunchedEffect(localSearchQuery, shiftList) {
        delay(250)
        val q = localSearchQuery.trim().lowercase()
        filteredList = if (q.isEmpty()) {
            shiftList
        } else {
            shiftList.filter { s ->
                s.namaShift.lowercase().contains(q) ||
                        s.jam_mulai.lowercase().contains(q) ||
                        s.jam_selesai.lowercase().contains(q) ||
                        (s.deskripsi?.lowercase()?.contains(q) == true)
            }
        }
    }

    // Snackbar untuk error
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            // kalau kamu punya clearError(), panggil di sini:
            // viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isLoading) {
                FloatingActionButton(onClick = { viewModel.openAddDialog() }) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Shift")
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
                            "Shift",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${filteredList.size} shift",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Search bar
                    OutlinedTextField(
                        value = localSearchQuery,
                        onValueChange = { localSearchQuery = it },
                        label = { Text("Cari shift, jam, deskripsi...") },
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

                    // List / Empty state
                    if (filteredList.isEmpty()) {
                        ShiftEmptyState(
                            modifier = Modifier.fillMaxSize(),
                            text = if (localSearchQuery.isNotEmpty()) {
                                "Tidak ditemukan shift dengan pencarian '$localSearchQuery'"
                            } else {
                                "Belum ada data shift"
                            }
                        )
                    } else {
                        LazyColumn {
                            items(filteredList) { shift ->
                                ShiftItem(
                                    data = shift,
                                    onEdit = { viewModel.openEditDialog(shift) },
                                    onDelete = { viewModel.deleteShift(shift.shift_id) }
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
        ShiftFormDialog(
            data = selectedShift,
            onDismiss = { viewModel.closeDialog() },
            onSave = { viewModel.saveShift(it) }
        )
    }
}

@Composable
private fun ShiftEmptyState(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.WorkOff,
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
