package com.example.foca_mobile.service

//import com.example.foca_mobile.utils.LoginPrefs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit


object ServiceGenerator {
    private const val BASE_URL = "https://foca-backend.herokuapp.com/"
    private val cacheSize = 5 * 1024 * 1024
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()
    private var retrofit: Retrofit? = null;

    fun <T> buildService(service: Class<T>):T{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!.create(service)
    }
}