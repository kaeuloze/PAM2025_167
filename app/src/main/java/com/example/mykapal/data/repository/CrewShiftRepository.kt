package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.CrewShift

class CrewShiftRepository {

    suspend fun getAll() = ApiClient.api.getCrewShift()

    suspend fun insert(data: CrewShift) =
        ApiClient.api.insertCrewShift(data)

    suspend fun update(data: CrewShift) =
        ApiClient.api.updateCrewShift(data)

    suspend fun delete(id: Int) =
        ApiClient.api.deleteCrewShift(mapOf("crew_shift_id" to id))
}
