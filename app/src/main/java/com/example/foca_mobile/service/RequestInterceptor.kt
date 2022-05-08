package com.example.foca_mobile.service

import android.util.Log
import com.example.foca_mobile.utils.LoginPrefs
import okhttp3.Interceptor
import okhttp3.Response


class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = LoginPrefs.getUser()?.accessToken
        val originalRequestBuilder = chain.request()
        val newRequestBuilder = originalRequestBuilder.newBuilder()
        if(accessToken != null){
            newRequestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }
        return chain.proceed(newRequestBuilder.build())
    }
}