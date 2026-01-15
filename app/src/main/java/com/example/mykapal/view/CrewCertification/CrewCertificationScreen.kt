package com.example.mykapal.view.CrewCertification

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykapal.viewmodel.CrewCertificationViewModel

@Composable
fun CrewCertificationScreen(
    viewModel: CrewCertificationViewModel = viewModel()
) {
    val list by viewModel.list.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LazyColumn {
        items(list) { item ->
            CrewCertificationItem(
                data = item,
                onEdit = {
                    viewModel.selected = item
                    viewModel.showDialog = true
                },
                onDelete = {
                    viewModel.delete(item.crew_cert_id)
                }
            )
        }
    }
}
