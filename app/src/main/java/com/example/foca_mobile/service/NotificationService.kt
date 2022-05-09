package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Notification
import retrofit2.Call
import retrofit2.http.GET

interface NotificationService {

    @GET("/api/buyer/notifications")
    fun getUserNotify(): Call<ApiResponse<MutableList<Notification>>>?

    @GET("/api/buyer/notifications/mark-all-seen")
    fun markAllSeen()

}