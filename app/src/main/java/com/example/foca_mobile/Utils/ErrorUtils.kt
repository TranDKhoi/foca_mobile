package com.example.foca_mobile.utils

import com.example.foca_mobile.model.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody


object ErrorUtils {
    fun parseHttpError(error: ResponseBody): ErrorResponse {
        var errorObject = Gson().fromJson(error.charStream(), ErrorResponse::class.java)
        return errorObject
    }
}