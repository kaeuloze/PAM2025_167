package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.ApiResponse
import com.example.mykapal.data.model.CrewCertification


class CrewCertificationRepository {

    // ===== GET ALL =====
    suspend fun getAll(): ApiResponse<List<CrewCertification>> =
        ApiClient.api.getCrewCertification()

    // ===== INSERT =====
    suspend fun insert(data: CrewCertification): ApiResponse<CrewCertification> =
        ApiClient.api.insertCrewCertification(
            crewCertId = data.crew_cert_id,
            crewId = data.crew_id,
            certificationId = data.certification_id,
            namaSertifikasi = data.nama_sertifikasi,
            kru = data.kru,
            tanggalTerbit = data.tanggal_terbit,
            tanggalKadaluarsa = data.tanggal_kadaluarsa,
            status = data.status
        )

    // ===== UPDATE =====
    suspend fun update(data: CrewCertification): ApiResponse<CrewCertification> =
        ApiClient.api.updateCrewCertification(
            crewCertId = data.crew_cert_id,
            crewId = data.crew_id,
            certificationId = data.certification_id,
            namaSertifikasi = data.nama_sertifikasi,
            kru = data.kru,
            tanggalTerbit = data.tanggal_terbit,
            tanggalKadaluarsa = data.tanggal_kadaluarsa,
            status = data.status
        )

    // ===== DELETE =====
    suspend fun delete(id: Int): ApiResponse<String> =
        ApiClient.api.deleteCrewCertification(
            crewCertId = id
        )

    // ===== OPTIONAL HELPER =====
    suspend fun save(data: CrewCertification): ApiResponse<CrewCertification> {
        return if (data.crew_cert_id == 0) insert(data) else update(data)
    }
}
