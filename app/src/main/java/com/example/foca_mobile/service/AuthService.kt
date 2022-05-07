package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthService {
    @POST("/api/auth/login")
    fun login(@Body user: User): Call<ApiResponse<User>>?

    @POST("/api/email/account-verification")
    fun sendEmailVerification(@Body requestBody: RequestBody): Call<ApiResponse<String>>?

    @POST("/api/email/verify-code")
    fun sendVerifyCode(@Body requestBody: RequestBody): Call<ApiResponse<String>>?

    @POST("/api/auth/register")
    fun registerUser(@Body requestBody: RequestBody): Call<ApiResponse<User>>?

    @GET("/api/auth")
    fun verifyAccessToken(@Header("Authorization")token: String): Call<ApiResponse<User>>?
}