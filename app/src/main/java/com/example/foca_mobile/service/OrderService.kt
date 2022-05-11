package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @GET("/api/admin/orders")
    fun getOrderByStatus(@Query("status") status: String): Call<ApiResponse<MutableList<Order>>>?

    @GET("/api/admin/orders")
    fun getAllOrder(@Query("limit") limit: Int): Call<ApiResponse<MutableList<Order>>>?

    @PUT("/api/admin/orders/{id}")
    fun updateOrderStatus(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): Call<ApiResponse<OrderDetails>>?

}