package com.example.foca_mobile.service

import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ProductService {
    @GET("/api/admin/products")
    fun getProductList(
        @Query("type") type: String?= null,
        @Query("limit") limit: Int? = null,
        @Query("price[gte]") price1: Int? = null,
        @Query("price[lte]") price2: Int?= null,
        @Query("sort") sort: String?= null,
    ): Call<ApiResponse<MutableList<Product>>>?

    @POST("/api/admin/products")
    fun createProduct(@Body requestBody: RequestBody): Call<ApiResponse<Product>>?

    @PUT("/api/admin/products/{id}")
    fun updateProduct(@Path("id") id: String, @Body body: RequestBody): Call<ApiResponse<Product>>?

    @DELETE("/api/admin/products/{id}")
    fun softDeleteProduct(@Path("id") id: String): Call<ApiResponse<String>>?

    @GET("/api/products")
    fun getUserProduct(): Call<ApiResponse<MutableList<Product>>>?
}