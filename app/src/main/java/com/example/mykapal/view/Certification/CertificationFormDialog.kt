@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mykapal.view.Certification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mykapal.data.model.Certification

@Composable
fun CertificationFormDialog(
    data: Certification?,
    onDismiss: () -> Unit,
    onSave: (Certification) -> Unit
) {
    // Pastikan nama field ini sesuai model kamu:
    // - data?.namaSertifikasi
    // - data?.deskripsi
    var nama by remember { mutableStateOf(data?.namaSertifikasi ?: "") }
    var deskripsi by remember { mutableStateOf(data?.deskripsi ?: "") }

    // ===== VALIDATION =====
    val namaOk = nama.trim().isNotEmpty()
    // deskripsi biasanya boleh kosong, tapi kalau backend kamu wajibkan, ubah jadi:
    // val deskripsiOk = deskripsi.trim().isNotEmpty()
    val deskripsiOk = true

    val isFormValid = namaOk && deskripsiOk

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 520.dp)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (data == null) "Tambah Sertifikasi" else "Edit Sertifikasi",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Sertifikasi") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    isError = !namaOk && nama.isNotBlank(),
                    supportingText = {
                        if (!namaOk && nama.isNotBlank()) Text("Nama sertifikasi wajib diisi")
                    }
                )

                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp),
                    maxLines = 4,
                    leadingIcon = { Icon(Icons.Default.Description, null) }
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val payload = Certification(
                                sertif_id = data?.sertif_id ?: 0,
                                namaSertifikasi = nama.trim(),
                                deskripsi = deskripsi.trim()
                            )
                            onSave(payload)
                        },
                        enabled = isFormValid
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (data == null) "Simpan" else "Update")
                    }
                }
            }
        }
    }
}
