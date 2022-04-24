package com.example.foca_mobile.retrofit

import android.content.Context
//import com.example.foca_mobile.utils.LoginPrefs
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit
import java.io.File


object ServiceGenerator {
    private val BASE_URL = "http://10.0.2.2:5000/"
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