package com.example.foca_mobile.service
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ProductService {
    @GET("/api/products")
    fun getProductList(): Call<ApiResponse<MutableList<Product>>>

    @POST("/api/products")
    fun createProduct(@Body user: Product?): Call<Product?>?
//    @POST("/api/posts")
//    fun createPost(): Call<MutableList<Post>>
}