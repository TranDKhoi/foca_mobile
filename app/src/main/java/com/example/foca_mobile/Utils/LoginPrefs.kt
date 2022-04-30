package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.model.User
import com.google.gson.Gson




object LoginPrefs {

    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(LoginScreen.appContext)

    const val USER_TOKEN = "usertoken";
    const val USER = "user";

    fun getUserToken(): String {
        return sharedPreferences.getString(USER_TOKEN, "").toString();
    }
    fun getUser(): User {
        val jsonUser = sharedPreferences.getString(USER, "").toString();
        val gson = Gson()
        val user: User = gson.fromJson(jsonUser, User::class.java)
        return user
    }

    fun setUser(user:User) {
        var editor = sharedPreferences.edit();
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString(USER, userJson);
        editor.apply();
    }

    fun setUserToken(token: String) {
        var editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    fun removeToken() {
        sharedPreferences.edit().clear().apply();
    }
}