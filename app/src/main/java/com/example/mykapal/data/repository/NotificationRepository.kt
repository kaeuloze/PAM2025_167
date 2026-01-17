package com.example.mykapal.data.repository


import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.NotificationModel

class NotificationRepository {

    suspend fun getAll(): List<NotificationModel> =
        ApiClient.api.getNotifications()
}

