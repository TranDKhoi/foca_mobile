package com.example.foca_mobile.Utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.authen.login.LoginScreen

object LoginPrefs {

    public var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(LoginScreen.appContext)

    const val USER_ID = "userid";


    fun getUserID(): String {
        return sharedPreferences.getString(USER_ID, "").toString();
    }

    fun setUserID(id: String) {

        var editor = sharedPreferences.edit();
        editor.putString(USER_ID, id);
        editor.apply();
    }

    fun removeID() {
        sharedPreferences.edit().clear().apply();
    }
}