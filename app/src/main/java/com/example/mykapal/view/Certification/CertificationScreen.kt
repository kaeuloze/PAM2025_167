package com.example.mykapal.view.Certification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykapal.viewmodel.CertificationViewModel

@Composable
fun CertificationScreen(
    viewModel: CertificationViewModel = viewModel()
) {
    val list by viewModel.list.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    if (viewModel.showDialog) {
        CertificationFormDialog(
            data = viewModel.selected,
            onDismiss = { viewModel.showDialog = false },
            onSave = { viewModel.save(it) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Master Sertifikasi",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.selected = null
                viewModel.showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Sertifikasi")
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(list) {
                CertificationItem(
                    data = it,
                    onEdit = {
                        viewModel.selected = it
                        viewModel.showDialog = true
                    },
                    onDelete = {
                        viewModel.delete(it.sertif_id)
                    }
                )
            }
        }
    }
}
