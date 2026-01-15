package com.example.mykapal.view.CrewCertification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mykapal.data.model.CrewCertification

@Composable
fun CrewCertificationItem(
    data: CrewCertification,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor =
        if (data.status == "Aktif") Color(0xFF2E7D32)
        else Color(0xFFC62828)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(data.kru, fontWeight = FontWeight.Bold)
            Text("Sertifikasi: ${data.nama_sertifikasi}")
            Text("Terbit: ${data.tanggal_terbit}")
            Text("Kadaluarsa: ${data.tanggal_kadaluarsa}")
            Text("Status: ${data.status}", color = statusColor)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onDelete) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
