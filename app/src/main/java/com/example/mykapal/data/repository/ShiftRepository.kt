package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.ApiResponse
import com.example.mykapal.data.model.Shift

class ShiftRepository {
    suspend fun get(): ApiResponse<List<Shift>> = ApiClient.api.getShift()
    suspend fun delete(id: Int) = ApiClient.api.deleteShift(mapOf("shift_id" to id))
    suspend fun insert(data: Shift) = ApiClient.api.insertShift(data)
    suspend fun update(data: Shift) = ApiClient.api.updateShift(data)
}


