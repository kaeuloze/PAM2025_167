package com.example.mykapal.view.crew

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
import com.example.mykapal.data.model.Crew

@Composable
fun CrewFormDialog(
    crew: Crew?,
    onDismiss: () -> Unit,
    onSave: (Crew) -> Unit
) {
    var nama by remember { mutableStateOf(crew?.nama_lengkap ?: "") }
    var jk by remember { mutableStateOf(crew?.jenis_kelamin ?: "") }
    var passport by remember { mutableStateOf(crew?.nomor_passport ?: "") }
    var posisi by remember { mutableStateOf(crew?.posisi ?: "") }
    var kontak by remember { mutableStateOf(crew?.kontak ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (crew == null) "Tambah Kru" else "Edit Kru") },
        text = {
            Column {
                OutlinedTextField(nama, { nama = it }, label = { Text("Nama") })
                OutlinedTextField(jk, { jk = it }, label = { Text("Jenis Kelamin (L/P)") })
                OutlinedTextField(passport, { passport = it }, label = { Text("Passport") })
                OutlinedTextField(posisi, { posisi = it }, label = { Text("Posisi") })
                OutlinedTextField(kontak, { kontak = it }, label = { Text("Kontak") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Crew(
                        crew_id = crew?.crew_id ?: 0,
                        nama_lengkap = nama,
                        jenis_kelamin = jk,
                        tanggal_lahir = crew?.tanggal_lahir ?: "",
                        kewarganegaraan = crew?.kewarganegaraan ?: "",
                        posisi = posisi,
                        nomor_passport = passport,
                        kontak = kontak,
                        alamat = crew?.alamat ?: ""
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
