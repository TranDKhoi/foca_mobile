package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.authen.login.LoginScreen

object LoginPrefs {

    public var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(LoginScreen.appContext)

    const val USER_TOKEN = "usertoken";

    fun getUserToken(): String {
        return sharedPreferences.getString(USER_TOKEN, "").toString();
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