@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mykapal.view.Shift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mykapal.data.model.Shift

@Composable
fun ShiftFormDialog(
    data: Shift?,
    onDismiss: () -> Unit,
    onSave: (Shift) -> Unit
) {
    // Model Shift kamu: shift_id, namaShift (@SerializedName("nama_shift")), jam_mulai, jam_selesai, deskripsi?
    var namaShift by remember { mutableStateOf(data?.namaShift ?: "") }
    var jamMulai by remember { mutableStateOf(data?.jam_mulai ?: "") }      // HH:mm
    var jamSelesai by remember { mutableStateOf(data?.jam_selesai ?: "") }  // HH:mm
    var deskripsi by remember { mutableStateOf(data?.deskripsi ?: "") }

    // ===== VALIDATION =====
    val timeRegex = remember { Regex("""^\d{2}:\d{2}$""") }

    val namaOk = namaShift.trim().isNotEmpty()
    val mulaiOk = timeRegex.matches(jamMulai.trim())
    val selesaiOk = timeRegex.matches(jamSelesai.trim())

    val isFormValid = namaOk && mulaiOk && selesaiOk

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 520.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (data == null) "Tambah Shift" else "Edit Shift",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = namaShift,
                    onValueChange = { namaShift = it },
                    label = { Text("Nama Shift") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { androidx.compose.material3.Icon(Icons.Default.Title, null) },
                    isError = !namaOk && namaShift.isNotBlank(),
                    supportingText = {
                        if (!namaOk && namaShift.isNotBlank()) Text("Nama shift wajib diisi")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = jamMulai,
                    onValueChange = { jamMulai = it },
                    label = { Text("Jam Mulai (HH:mm)") },
                    placeholder = { Text("Contoh: 08:00") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { androidx.compose.material3.Icon(Icons.Default.AccessTime, null) },
                    isError = jamMulai.isNotBlank() && !mulaiOk,
                    supportingText = {
                        if (jamMulai.isNotBlank() && !mulaiOk) Text("Format harus HH:mm (contoh 08:00)")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = jamSelesai,
                    onValueChange = { jamSelesai = it },
                    label = { Text("Jam Selesai (HH:mm)") },
                    placeholder = { Text("Contoh: 16:00") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { androidx.compose.material3.Icon(Icons.Default.Schedule, null) },
                    isError = jamSelesai.isNotBlank() && !selesaiOk,
                    supportingText = {
                        if (jamSelesai.isNotBlank() && !selesaiOk) Text("Format harus HH:mm (contoh 16:00)")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi (opsional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp),
                    maxLines = 3,
                    leadingIcon = { androidx.compose.material3.Icon(Icons.Default.Description, null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(Modifier.width(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val newShift = Shift(
                                shift_id = data?.shift_id ?: 0,
                                namaShift = namaShift.trim(),
                                jam_mulai = jamMulai.trim(),
                                jam_selesai = jamSelesai.trim(),
                                deskripsi = deskripsi.trim().ifEmpty { null }
                            )
                            onSave(newShift)
                        },
                        enabled = isFormValid
                    ) {
                        Text(if (data == null) "Simpan" else "Update")
                    }
                }

                if (!mulaiOk && jamMulai.isNotBlank()) {
                    Text(
                        text = "Jam mulai harus format HH:mm (contoh 08:00)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (!selesaiOk && jamSelesai.isNotBlank()) {
                    Text(
                        text = "Jam selesai harus format HH:mm (contoh 16:00)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
