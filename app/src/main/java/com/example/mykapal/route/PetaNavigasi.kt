package com.example.mykapal.route


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mykapal.view.Certification.CertificationScreen
import com.example.mykapal.view.CrewCertification.CrewCertificationScreen

import com.example.mykapal.view.CrewShift.CrewShiftScreen
import com.example.mykapal.view.LoginScreen
import com.example.mykapal.view.Notification.NotificationScreen
import com.example.mykapal.view.Shift.ShiftScreen
import com.example.mykapal.view.crew.CrewScreen
import com.example.mykapal.view.dashboard.DashboardScreen


@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier   // 🔥 INI YANG PENTING
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(Routes.CREW) {
            CrewScreen()
        }

        composable(Routes.CERTIFICATION) {
            CertificationScreen()
        }

        composable(Routes.CREW_CERTIFICATION) {
            CrewCertificationScreen()
        }

        composable(Routes.SHIFT) {
            ShiftScreen()
        }

        composable(Routes.CREW_SHIFT) {
            CrewShiftScreen()
        }

        composable(Routes.NOTIFICATION) {
            NotificationScreen()
        }
    }
}
