package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.model.User
import com.google.gson.Gson




object LoginPrefs {

    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(LoginScreen.appContext)

    const val USER_TOKEN = "usertoken"
    const val USER = "user"

    fun getUserToken(): String {
        return sharedPreferences.getString(USER_TOKEN, "").toString()
    }
    fun getUser(): User {
        val jsonUser = sharedPreferences.getString(USER, "").toString()
        if (jsonUser == "") return User("",)
        val gson = Gson()
        return gson.fromJson(jsonUser, User::class.java)
    }

    fun setUser(user:User) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString(USER, userJson)
        editor.apply()
    }

    fun setUserToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun removeToken() {
        sharedPreferences.edit().remove("user").apply()
        sharedPreferences.edit().remove("usertoken").apply()
    }
}