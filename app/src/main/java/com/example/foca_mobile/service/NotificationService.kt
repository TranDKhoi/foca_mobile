package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Notification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {

    @GET("/api/buyer/notifications")
    fun getUserNotify(@Query("isSeen") isSeen: String? = null): Call<ApiResponse<MutableList<Notification>>>?

    @GET("/api/buyer/notifications/mark-all-seen")
    fun markAllSeen()

}