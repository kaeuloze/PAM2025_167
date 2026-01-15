package com.example.mykapal.view.Certification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import com.example.mykapal.data.model.Certification

@Composable
fun CertificationFormDialog(
    data: Certification?,
    onDismiss: () -> Unit,
    onSave: (Certification) -> Unit
) {
    var nama by remember { mutableStateOf(data?.namaSertifikasi ?: "") }
    var deskripsi by remember { mutableStateOf(data?.deskripsi ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (data == null) "Tambah Sertifikasi" else "Edit Sertifikasi") },
        text = {
            Column {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Sertifikasi") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Certification(
                        sertif_id = data?.sertif_id ?: 0,
                        namaSertifikasi = nama,
                        deskripsi = deskripsi
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
