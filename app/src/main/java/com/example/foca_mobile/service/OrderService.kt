package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @GET("/api/admin/orders")
    fun getOrderByStatus(@Query("status") status: String): Call<ApiResponse<MutableList<Order>>>?

    @GET("/api/admin/orders")
    fun getAllOrder(): Call<ApiResponse<MutableList<Order>>>?

    @PUT("/api/admin/orders/{id}")
    fun updateOrderStatus(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Call<ApiResponse<Int>>?

}