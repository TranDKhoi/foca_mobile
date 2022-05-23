package com.example.foca_mobile.service

import com.example.foca_mobile.model.*
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

    @GET("/api/products")
    fun getUserProductList(
        @Query("type") type: String?= null,
        @Query("limit") limit: Int? = null,
        @Query("price[gte]") price1: Int? = null,
        @Query("price[lte]") price2: Int?= null,
        @Query("sort") sort: String?= null,
        @Query("q") q: String?= null,
    ): Call<ApiResponse<MutableList<Product>>>?

    @POST("/api/admin/products")
    fun createProduct(@Body requestBody: RequestBody): Call<ApiResponse<Product>>?

    @PUT("/api/admin/products/{id}")
    fun updateProduct(@Path("id") id: String, @Body body: RequestBody): Call<ApiResponse<Product>>?

    @DELETE("/api/admin/products/{id}")
    fun softDeleteProduct(@Path("id") id: String): Call<ApiResponse<String>>?

    @GET("/api/products/{id}")
    fun getProductDetails(@Path("id") id : Int) : Call<ApiResponse<ProductDetails>>?

    @GET("/api/products/{id}/reviews")
    fun getAllReview(@Path("id") id : Int) : Call<ApiResponse<MutableList<Review>>>?

    @GET("/api/products/{id}/reviews/stats")
    fun getReviewStatistic(@Path("id") id : Int) : Call<ApiResponse<MutableList<ReviewStatistic>>>?
}