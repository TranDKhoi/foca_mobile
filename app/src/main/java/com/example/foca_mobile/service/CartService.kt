package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Cart
import com.example.foca_mobile.model.Order
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CartService {
    @POST("/api/me/cart-items")
    fun createCart(@Body requestBody: RequestBody) : Call<ApiResponse<Cart>>

    @GET("/api/me/cart-items")
    fun getUserCart() : Call<ApiResponse<MutableList<Cart>>>

    @POST("/api/buyer/orders")
    fun createOrder(@Body requestBody: RequestBody) : Call<ApiResponse<Order>>

    @PUT("/api/me/cart-items/{cartId}")
    fun updateCart(@Body requestBody: RequestBody, @Path("cartId") cartId : Int) : Call<ApiResponse<Cart>>

    @HTTP(method = "DELETE", path = "/api/me/cart-items/{cartId}", hasBody = true)
    fun deleteCart(@Body requestBody: RequestBody, @Path("cartId") cartId : Int) : Call<ApiResponse<Int>>

}