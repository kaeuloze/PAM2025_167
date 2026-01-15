package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.CrewCertification

class CrewCertificationRepository {

    suspend fun getAll() =
        ApiClient.api.getCrewCertification()

    suspend fun insert(data: CrewCertification) =
        ApiClient.api.insertCrewCertification(data)

    suspend fun update(data: CrewCertification) =
        ApiClient.api.updateCrewCertification(data)

    suspend fun delete(id: Int) =
        ApiClient.api.deleteCrewCertification(mapOf("crew_cert_id" to id))
}
