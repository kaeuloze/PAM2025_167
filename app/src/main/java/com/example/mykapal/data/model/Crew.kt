package com.example.mykapal.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Crew(
    val crew_id: Int,
    val nama_lengkap: String,
    val jenis_kelamin: String,
    val tanggal_lahir: String,
    val kewarganegaraan: String,
    val posisi: String,
    val nomor_passport: String,
    val kontak: String,
    val alamat: String
)
