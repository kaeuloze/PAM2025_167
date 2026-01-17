package com.example.mykapal.view.CrewShift

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mykapal.data.model.CrewShift

@Composable
fun CrewShiftFormDialog(
    data: CrewShift?,
    onDismiss: () -> Unit,
    onSave: (CrewShift) -> Unit
) {
    var kru by remember { mutableStateOf(data?.kru ?: "") }
    var shift by remember { mutableStateOf(data?.shift ?: "") }
    var jamMulai by remember { mutableStateOf(data?.jam_mulai ?: "") }
    var jamSelesai by remember { mutableStateOf(data?.jam_selesai ?: "") }
    var tanggal by remember { mutableStateOf(data?.tanggal_shift ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Form Crew Shift") },
        text = {
            Column {
                OutlinedTextField(kru, { kru = it }, label = { Text("Nama Kru") })
                OutlinedTextField(shift, { shift = it }, label = { Text("Shift") })
                OutlinedTextField(jamMulai, { jamMulai = it }, label = { Text("Jam Mulai") })
                OutlinedTextField(jamSelesai, { jamSelesai = it }, label = { Text("Jam Selesai") })
                OutlinedTextField(tanggal, { tanggal = it }, label = { Text("Tanggal") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    CrewShift(
                        crew_shift_id = data?.crew_shift_id ?: 0,
                        kru = kru,
                        shift = shift,
                        jam_mulai = jamMulai,
                        jam_selesai = jamSelesai,
                        tanggal_shift = tanggal
                    )
                )
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
