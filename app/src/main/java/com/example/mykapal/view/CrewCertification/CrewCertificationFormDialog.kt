@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mykapal.view.CrewCertification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.mykapal.data.model.Certification
import com.example.mykapal.data.model.Crew
import com.example.mykapal.data.model.CrewCertification
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CrewCertificationFormDialog(
    data: CrewCertification?,
    crewList: List<Crew>,
    certList: List<Certification>,
    onDismiss: () -> Unit,
    onSave: (CrewCertification) -> Unit
) {
    // ===== State =====
    var crewId by remember { mutableStateOf(data?.crew_id ?: 0) }
    var certId by remember { mutableStateOf(data?.certification_id ?: 0) }

    var terbit by remember { mutableStateOf(data?.tanggal_terbit ?: "") }          // YYYY-MM-DD
    var kadaluarsa by remember { mutableStateOf(data?.tanggal_kadaluarsa ?: "") }  // YYYY-MM-DD

    // optional: status bisa diedit atau auto-generate
    var status by remember { mutableStateOf(data?.status ?: "") }

    var crewExpanded by remember { mutableStateOf(false) }
    var certExpanded by remember { mutableStateOf(false) }

    val selectedCrewLabel = remember(crewId, crewList) {
        crewList.firstOrNull { it.crew_id == crewId }?.nama_lengkap ?: ""
    }
    val selectedCertLabel = remember(certId, certList) {
        certList.firstOrNull { it.sertif_id == certId }?.namaSertifikasi ?: ""
    }

    // ===== Validation =====
    val dateRegex = remember { Regex("""\d{4}-\d{2}-\d{2}""") }

    val crewOk = crewId > 0
    val certOk = certId > 0
    val terbitOk = terbit.trim().isNotEmpty() && dateRegex.matches(terbit.trim())
    val kadOk = kadaluarsa.trim().isNotEmpty() && dateRegex.matches(kadaluarsa.trim())

    val terbitDate = remember(terbit) { parseDateOrNull(terbit.trim()) }
    val kadDate = remember(kadaluarsa) { parseDateOrNull(kadaluarsa.trim()) }
    val rangeOk = terbitDate != null && kadDate != null && !kadDate.isBefore(terbitDate)

    val isFormValid = crewOk && certOk && terbitOk && kadOk && rangeOk

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
                    text = if (data == null) "Tambah Sertifikasi Kru" else "Edit Sertifikasi Kru",
                    style = MaterialTheme.typography.headlineSmall
                )

                // ===== Dropdown Kru =====
                ExposedDropdownMenuBox(
                    expanded = crewExpanded,
                    onExpandedChange = { crewExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCrewLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih Kru") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = crewExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        isError = !crewOk && selectedCrewLabel.isNotBlank()
                    )

                    ExposedDropdownMenu(
                        expanded = crewExpanded,
                        onDismissRequest = { crewExpanded = false }
                    ) {
                        crewList.forEach { crew ->
                            DropdownMenuItem(
                                text = { Text(crew.nama_lengkap) },
                                onClick = {
                                    crewId = crew.crew_id
                                    crewExpanded = false
                                },
                                leadingIcon = { Icon(Icons.Default.Person, null) }
                            )
                        }
                    }
                }

                if (!crewOk && selectedCrewLabel.isBlank()) {
                    Text(
                        text = "Kru wajib dipilih",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // ===== Dropdown Sertifikasi =====
                ExposedDropdownMenuBox(
                    expanded = certExpanded,
                    onExpandedChange = { certExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCertLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih Sertifikasi") },
                        leadingIcon = { Icon(Icons.Default.Verified, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = certExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        isError = !certOk && selectedCertLabel.isNotBlank()
                    )

                    ExposedDropdownMenu(
                        expanded = certExpanded,
                        onDismissRequest = { certExpanded = false }
                    ) {
                        certList.forEach { cert ->
                            DropdownMenuItem(
                                text = { Text(cert.namaSertifikasi) },
                                onClick = {
                                    certId = cert.sertif_id
                                    certExpanded = false
                                },
                                leadingIcon = { Icon(Icons.Default.Verified, null) }
                            )
                        }
                    }
                }

                if (!certOk && selectedCertLabel.isBlank()) {
                    Text(
                        text = "Sertifikasi wajib dipilih",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // ===== Tanggal Terbit =====
                OutlinedTextField(
                    value = terbit,
                    onValueChange = { terbit = it },
                    label = { Text("Tanggal Terbit (YYYY-MM-DD)") },
                    placeholder = { Text("Contoh: 2026-01-17") },
                    leadingIcon = { Icon(Icons.Default.Event, null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = terbit.isNotBlank() && !terbitOk,
                    supportingText = {
                        if (terbit.isNotBlank() && !terbitOk) Text("Format harus YYYY-MM-DD")
                    }
                )

                // ===== Tanggal Kadaluarsa =====
                OutlinedTextField(
                    value = kadaluarsa,
                    onValueChange = { kadaluarsa = it },
                    label = { Text("Tanggal Kadaluarsa (YYYY-MM-DD)") },
                    placeholder = { Text("Contoh: 2027-01-17") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = kadaluarsa.isNotBlank() && !kadOk,
                    supportingText = {
                        if (kadaluarsa.isNotBlank() && !kadOk) Text("Format harus YYYY-MM-DD")
                    }
                )

                if (terbitOk && kadOk && !rangeOk) {
                    Text(
                        text = "Tanggal kadaluarsa harus setelah tanggal terbit",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // ===== Status (auto / bisa diedit) =====
                // Kalau kamu mau status auto, field ini bisa dihapus.
                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("Status (opsional)") },
                    placeholder = { Text("Aktif / Kadaluarsa") },
                    leadingIcon = { Icon(Icons.Default.Info, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // ===== Buttons =====
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Batal") }
                    Spacer(Modifier.width(8.dp))

                    Button(
                        enabled = isFormValid,
                        onClick = {
                            val today = LocalDate.now()
                            val expiry = kadDate ?: today
                            val autoStatus = if (!expiry.isBefore(today)) "Aktif" else "Kadaluarsa"

                            val finalStatus = status.trim().ifBlank { autoStatus }

                            // kalau model kamu butuh field nama_sertifikasi & kru juga,
                            // isi dari label pilihan
                            val newData = CrewCertification(
                                crew_cert_id = data?.crew_cert_id ?: 0,
                                crew_id = crewId,
                                certification_id = certId,
                                nama_sertifikasi = selectedCertLabel.ifBlank { data?.nama_sertifikasi ?: "" },
                                kru = selectedCrewLabel.ifBlank { data?.kru ?: "" },
                                tanggal_terbit = terbit.trim(),
                                tanggal_kadaluarsa = kadaluarsa.trim(),
                                status = finalStatus
                            )

                            onSave(newData)
                        }
                    ) {
                        Text(if (data == null) "Simpan" else "Update")
                    }
                }
            }
        }
    }
}

private fun parseDateOrNull(value: String): LocalDate? {
    return try {
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(value, fmt)
    } catch (_: Exception) {
        null
    }
}
