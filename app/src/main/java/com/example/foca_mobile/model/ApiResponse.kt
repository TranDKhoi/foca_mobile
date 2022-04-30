package com.example.foca_mobile.model

data class ApiResponse<T>(
    val message: String,
    val data: T
)
