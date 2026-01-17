package com.example.mykapal.view.CrewShift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykapal.viewmodel.CrewShiftViewModel

@Composable
fun CrewShiftScreen(
    viewModel: CrewShiftViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val list by viewModel.list.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    if (viewModel.showDialog) {
        CrewShiftFormDialog(
            data = viewModel.selected,
            onDismiss = { viewModel.showDialog = false },
            onSave = { viewModel.save(it) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Jadwal Kerja Kru",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.selected = null
                viewModel.showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Shift Kru")
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(list) { crewShift ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(crewShift.kru, fontWeight = FontWeight.Bold)
                        Text("Shift: ${crewShift.shift}")
                        Text("${crewShift.jam_mulai} - ${crewShift.jam_selesai}")
                        Text("Tanggal: ${crewShift.tanggal_shift}")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                viewModel.selected = crewShift
                                viewModel.showDialog = true
                            }) {
                                Text("Edit")
                            }
                            TextButton(onClick = {
                                viewModel.delete(crewShift.crew_shift_id)
                            }) {
                                Text("Hapus", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
