package com.example.mykapal.view.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mykapal.route.Routes


@Composable
fun DashboardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Dashboard",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        DashboardMenu("Data Kru") {
            navController.navigate(Routes.CREW)
        }

        DashboardMenu("Sertifikasi") {
            navController.navigate(Routes.CERTIFICATION)
        }

        DashboardMenu("Sertifikasi Kru") {
            navController.navigate(Routes.CREW_CERTIFICATION)
        }

        DashboardMenu("Shift Kerja") {
            navController.navigate(Routes.SHIFT)
        }


        DashboardMenu("Notifikasi") {
            navController.navigate(Routes.NOTIFICATION)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.DASHBOARD) { inclusive = true }
                }
            }
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun DashboardMenu(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

