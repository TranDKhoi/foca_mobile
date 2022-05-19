package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface InterestedProductService {
    @POST("/api/buyer/interested-products")
    fun addInterestedProduct(@Body requestBody : RequestBody) : Call<ApiResponse<Int>>?

    @DELETE("/api/buyer/interested-products/{id}")
    fun deleteInterestedProduct(@Path("id") id : Int) : Call<ApiResponse<Int>>?
}