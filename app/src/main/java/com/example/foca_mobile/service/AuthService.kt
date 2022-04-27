package com.example.foca_mobile.service
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.User
import retrofit2.Call
import retrofit2.http.*


interface AuthService {
    @POST("/api/auth/login")
    fun login(@Body user:User): Call<ApiResponse<User>>?
}