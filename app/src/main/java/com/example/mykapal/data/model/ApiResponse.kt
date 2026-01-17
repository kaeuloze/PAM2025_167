package com.example.mykapal.data.model

import kotlinx.serialization.SerialName

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("count") val count: Int? = null
)
