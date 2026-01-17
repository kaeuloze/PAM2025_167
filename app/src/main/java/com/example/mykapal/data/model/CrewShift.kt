package com.example.mykapal.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CrewShift(
    val crew_shift_id: Int = 0,
    val crew_id: Int = 0,
    val shift_id: Int = 0,
    val kru: String = "",
    val shift: String = "",
    val jam_mulai: String = "",
    val jam_selesai: String = "",
    val tanggal_shift: String = "",
    val status: String = ""
)
