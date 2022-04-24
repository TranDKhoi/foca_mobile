package com.example.foca_mobile.retrofit
import com.example.foca_mobile.models.Post
import retrofit2.Call
import retrofit2.http.GET

interface PostService {
    @GET("/api/posts")
    fun getPostList(): Call<MutableList<Post>>
}