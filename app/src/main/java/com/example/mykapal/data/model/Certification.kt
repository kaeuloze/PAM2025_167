package com.example.mykapal.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Certification(
    val sertif_id: Int = 0,
    val namaSertifikasi: String = "",
    val deskripsi: String = ""
)
