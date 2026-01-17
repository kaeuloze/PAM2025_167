package com.example.mykapal.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.67.64.207/kapalpesiar/"

    // 1. Konfigurasi Json (Lenient membantu jika JSON dari PHP kurang rapi)
    private val json = Json {
        ignoreUnknownKeys = true // Agar tidak error jika ada field baru dari API
        isLenient = true         // Membantu menangani format JSON yang kurang baku
    }

    // 2. Tentukan Content-Type
    private val contentType = "application/json".toMediaType()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}