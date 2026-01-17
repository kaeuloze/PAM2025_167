package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.DashboardSummary
import com.example.mykapal.data.model.NotificationModel

class DashboardRepository {

    suspend fun getSummary(): DashboardSummary {
        return ApiClient.api.getDashboardSummary()
    }

    suspend fun getNotification(): List<NotificationModel> {
        return ApiClient.api.getNotifications()
    }
}
