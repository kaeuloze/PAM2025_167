package com.example.mykapal.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DashboardSummary(
    val totalCrew: Int,
    val sertifAktif: Int,
    val sertifHampirKadaluarsa: Int,
    val shiftHariIni: Int
)
