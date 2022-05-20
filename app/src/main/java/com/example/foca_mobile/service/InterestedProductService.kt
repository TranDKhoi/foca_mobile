package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface InterestedProductService {

    @GET("/api/buyer/interested-products")
    fun getAllInterestedProduct() : Call<ApiResponse<MutableList<Product>>>?

    @POST("/api/buyer/interested-products")
    fun addInterestedProduct(@Body requestBody : RequestBody) : Call<ApiResponse<Int>>?

    @DELETE("/api/buyer/interested-products/{id}")
    fun deleteInterestedProduct(@Path("id") id : Int) : Call<ApiResponse<Int>>?
}