package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.SplashScreen

object NightModePrefs {

    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(SplashScreen.appContext)

    const val NIGHT = "night"

    fun getNightMode(): String {
        return sharedPreferences.getString(NIGHT, "").toString()
    }


    fun setNightMode(night: String) {
        val editor = sharedPreferences.edit()
        editor.putString(NIGHT, night)
        editor.apply()
    }

    fun removeNightMode() {
        sharedPreferences.edit().clear().apply()
    }
}