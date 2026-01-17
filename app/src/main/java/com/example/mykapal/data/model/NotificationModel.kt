package com.example.mykapal.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationModel(
    val notif_id: Int = 0,
    val crew_id: Int = 0,
    val tanggal_notif: String = "",
    val jenis: String = "",
    val pesan: String = ""
)

