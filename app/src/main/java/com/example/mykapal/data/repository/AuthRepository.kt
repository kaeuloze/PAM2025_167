package com.example.mykapal.data.repository

import com.example.mykapal.data.ApiClient
import com.example.mykapal.data.model.LoginRequest
import com.example.mykapal.data.model.LoginResponse

class AuthRepository {
    suspend fun login(username: String, password: String): LoginResponse {
        return ApiClient.api.login(LoginRequest (username, password))
    }
}
