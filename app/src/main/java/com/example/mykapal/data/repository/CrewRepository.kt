package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.ApiResponse
import com.example.mykapal.data.model.Crew

class CrewRepository {

    suspend fun getAll(): ApiResponse<List<Crew>> =
        ApiClient.api.getCrew()

    suspend fun insert(data: Crew): ApiResponse<Crew> =
        ApiClient.api.insertCrew(
            namaLengkap = data.nama_lengkap,
            jenisKelamin = data.jenis_kelamin,
            tanggalLahir = data.tanggal_lahir,
            kewarganegaraan = data.kewarganegaraan,
            posisi = data.posisi,
            nomorPassport = data.nomor_passport,
            kontak = data.kontak,
            alamat = data.alamat
        )

    suspend fun update(data: Crew): ApiResponse<Crew> =
        ApiClient.api.updateCrew(
            crewId = data.crew_id,
            namaLengkap = data.nama_lengkap,
            jenisKelamin = data.jenis_kelamin,
            tanggalLahir = data.tanggal_lahir,
            kewarganegaraan = data.kewarganegaraan,
            posisi = data.posisi,
            nomorPassport = data.nomor_passport,
            kontak = data.kontak,
            alamat = data.alamat
        )

    suspend fun delete(id: Int): ApiResponse<String> =
        ApiClient.api.deleteCrew(id)

    // Optional: helper save (insert/update otomatis)
    suspend fun save(data: Crew): ApiResponse<Crew> {
        return if (data.crew_id == 0) insert(data) else update(data)
    }
}
