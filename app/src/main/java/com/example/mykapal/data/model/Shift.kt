package com.example.mykapal.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    val shift_id: Int,
    val namaShift: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val deskripsi: String? = null
)
