package com.example.mykapal.view.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mykapal.data.model.NotificationModel

@Composable
fun NotificationItem(
    notification: NotificationModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = notification.jenis,
                fontWeight = FontWeight.Bold
            )

            Text(notification.pesan)

            Text(
                text = notification.tanggal_notif,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
