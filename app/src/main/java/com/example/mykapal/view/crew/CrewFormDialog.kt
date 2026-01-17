@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mykapal.view.crew

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mykapal.data.model.Crew


@Composable
fun CrewFormDialog(
    crew: Crew?,
    onDismiss: () -> Unit,
    onSave: (Crew) -> Unit
) {
    var namaLengkap by remember { mutableStateOf(crew?.nama_lengkap ?: "") }
    var jenisKelamin by remember { mutableStateOf(crew?.jenis_kelamin ?: "L") } // L / P
    var tanggalLahir by remember { mutableStateOf(crew?.tanggal_lahir ?: "") } // YYYY-MM-DD
    var kewarganegaraan by remember { mutableStateOf(crew?.kewarganegaraan ?: "") }
    var posisi by remember { mutableStateOf(crew?.posisi ?: "") }
    var nomorPassport by remember { mutableStateOf(crew?.nomor_passport ?: "") }
    var kontak by remember { mutableStateOf(crew?.kontak ?: "") }
    var alamat by remember { mutableStateOf(crew?.alamat ?: "") }

    var isGenderExpanded by remember { mutableStateOf(false) }

    // ===== VALIDATION =====
    val dateRegex = remember { Regex("""\d{4}-\d{2}-\d{2}""") }

    val namaOk = namaLengkap.trim().isNotEmpty()
    val posisiOk = posisi.trim().isNotEmpty()
    val jkOk = jenisKelamin.trim() in listOf("L", "P")
    val tglOk = dateRegex.matches(tanggalLahir.trim())
    val wargaOk = kewarganegaraan.trim().isNotEmpty()
    val passOk = nomorPassport.trim().isNotEmpty()
    val kontakOk = kontak.trim().isNotEmpty()
    val alamatOk = alamat.trim().isNotEmpty()

    val isFormValid = namaOk && posisiOk && jkOk && tglOk && wargaOk && passOk && kontakOk && alamatOk

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp)
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
                    text = if (crew == null) "Tambah Kru Baru" else "Edit Kru",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = namaLengkap,
                    onValueChange = { namaLengkap = it },
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    isError = !namaOk && namaLengkap.isNotBlank(),
                    supportingText = {
                        if (!namaOk && namaLengkap.isNotBlank()) Text("Nama wajib diisi")
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = posisi,
                        onValueChange = { posisi = it },
                        label = { Text("Posisi") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Default.Work, null) },
                        isError = !posisiOk && posisi.isNotBlank(),
                        supportingText = {
                            if (!posisiOk && posisi.isNotBlank()) Text("Posisi wajib diisi")
                        }
                    )

                    ExposedDropdownMenuBox(
                        expanded = isGenderExpanded,
                        onExpandedChange = { isGenderExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = if (jenisKelamin == "L") "Laki-laki (L)" else "Perempuan (P)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jenis Kelamin") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            leadingIcon = {
                                Icon(
                                    if (jenisKelamin == "L") Icons.Default.Male else Icons.Default.Female,
                                    null
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = isGenderExpanded,
                            onDismissRequest = { isGenderExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("L (Laki-laki)") },
                                onClick = {
                                    jenisKelamin = "L"
                                    isGenderExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("P (Perempuan)") },
                                onClick = {
                                    jenisKelamin = "P"
                                    isGenderExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = tanggalLahir,
                    onValueChange = { tanggalLahir = it },
                    label = { Text("Tanggal Lahir (YYYY-MM-DD)") },
                    placeholder = { Text("Contoh: 2016-01-01") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Cake, null) },
                    isError = tanggalLahir.isNotBlank() && !tglOk,
                    supportingText = {
                        if (tanggalLahir.isNotBlank() && !tglOk) Text("Format harus YYYY-MM-DD")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = kewarganegaraan,
                    onValueChange = { kewarganegaraan = it },
                    label = { Text("Kewarganegaraan") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Flag, null) },
                    isError = !wargaOk && kewarganegaraan.isNotBlank(),
                    supportingText = {
                        if (!wargaOk && kewarganegaraan.isNotBlank()) Text("Wajib diisi")
                    }
                )

                OutlinedTextField(
                    value = nomorPassport,
                    onValueChange = { nomorPassport = it },
                    label = { Text("Nomor Passport") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.CreditCard, null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                    isError = !passOk && nomorPassport.isNotBlank(),
                    supportingText = {
                        if (!passOk && nomorPassport.isNotBlank()) Text("Wajib diisi")
                    }
                )

                OutlinedTextField(
                    value = kontak,
                    onValueChange = { kontak = it },
                    label = { Text("Kontak (No. HP / Email)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    isError = !kontakOk && kontak.isNotBlank(),
                    supportingText = {
                        if (!kontakOk && kontak.isNotBlank()) Text("Wajib diisi")
                    }
                )

                OutlinedTextField(
                    value = alamat,
                    onValueChange = { alamat = it },
                    label = { Text("Alamat") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp),
                    maxLines = 3,
                    leadingIcon = { Icon(Icons.Default.Home, null) },
                    isError = !alamatOk && alamat.isNotBlank(),
                    supportingText = {
                        if (!alamatOk && alamat.isNotBlank()) Text("Wajib diisi")
                    }
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
                            val newCrew = Crew(
                                crew_id = crew?.crew_id ?: 0,
                                nama_lengkap = namaLengkap.trim(),
                                jenis_kelamin = jenisKelamin.trim().uppercase().take(1), // L / P
                                tanggal_lahir = tanggalLahir.trim(),
                                kewarganegaraan = kewarganegaraan.trim(),
                                posisi = posisi.trim(),
                                nomor_passport = nomorPassport.trim(),
                                kontak = kontak.trim(),
                                alamat = alamat.trim()
                            )
                            onSave(newCrew)
                        },
                        enabled = isFormValid
                    ) {
                        Text(if (crew == null) "Simpan" else "Update")
                    }
                }

                if (!jkOk) {
                    Text(
                        text = "Jenis kelamin harus L atau P",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
