package com.example.mykapal.view.CrewCertification

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
import com.example.mykapal.data.model.Certification
import com.example.mykapal.data.model.Crew
import com.example.mykapal.data.model.CrewCertification
import java.time.LocalDate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier

@Composable
fun CrewCertificationFormDialog(
    data: CrewCertification?,
    crewList: List<Crew>,
    certList: List<Certification>,
    onDismiss: () -> Unit,
    onSave: (CrewCertification) -> Unit
) {
    var crewId by remember { mutableStateOf(data?.crew_id ?: 0) }
    var certId by remember { mutableStateOf(data?.certification_id ?: 0) }
    var terbit by remember { mutableStateOf(data?.tanggal_terbit ?: "") }
    var kadaluarsa by remember { mutableStateOf(data?.tanggal_kadaluarsa ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (data == null) "Tambah Sertifikasi Kru" else "Edit Sertifikasi Kru") },
        text = {
            Column {
                DropdownMenuBox("Pilih Kru", crewList.map { it.nama_lengkap }) {
                    crewId = crewList[it].crew_id
                }
                DropdownMenuBox("Pilih Sertifikasi", certList.map { it.namaSertifikasi }) {
                    certId = certList[it].sertif_id
                }
                OutlinedTextField(terbit, { terbit = it }, label = { Text("Tanggal Terbit (yyyy-mm-dd)") })
                OutlinedTextField(kadaluarsa, { kadaluarsa = it }, label = { Text("Tanggal Kadaluarsa (yyyy-mm-dd)") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val status = if (kadaluarsa >= LocalDate.now().toString())
                    "Aktif" else "Kadaluarsa"

                onSave(
                    CrewCertification(
                        crew_cert_id = data?.crew_cert_id ?: 0,
                        crew_id = crewId,
                        certification_id = certId,
                        tanggal_terbit = terbit,
                        tanggal_kadaluarsa = kadaluarsa,
                        status = status
                    )
                )
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun DropdownMenuBox(
    label: String,
    items: List<String>,
    onItemSelected: (index: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )

        Box {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text) },
                        onClick = {
                            selectedText = text
                            expanded = false
                            onItemSelected(index)
                        }
                    )
                }
            }
        }
    }
}
