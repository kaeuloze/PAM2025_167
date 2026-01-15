package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.Certification

class CertificationRepository {

    suspend fun getAll() =
        ApiClient.api.getCertification()

    suspend fun insert(data: Certification) =
        ApiClient.api.insertCertification(data)

    suspend fun update(data: Certification) =
        ApiClient.api.updateCertification(data)

    suspend fun delete(id: Int) =
        ApiClient.api.deleteCertification(mapOf("sertif_id" to id))
}
