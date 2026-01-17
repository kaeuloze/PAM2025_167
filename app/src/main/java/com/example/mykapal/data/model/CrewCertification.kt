package com.example.mykapal.data.model

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
data class CrewCertification(
    val crew_cert_id: Int = 0,
    val crew_id: Int = 0,
    val certification_id: Int = 0,
    val nama_sertifikasi: String = "",
    val kru: String = "",
    val tanggal_terbit: String = "",
    val tanggal_kadaluarsa: String = "",
    val status: String = ""
)