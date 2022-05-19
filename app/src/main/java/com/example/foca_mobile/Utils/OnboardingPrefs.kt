package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.SplashScreen

object OnboardingPrefs {

    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(SplashScreen.appContext)

    const val FIRST_TIME = "firsttime"

    fun getFirsttime(): String {
        return sharedPreferences.getString(FIRST_TIME, "").toString()
    }

    fun setFirsttime(ft: String) {
        val editor = sharedPreferences.edit()
        editor.putString(FIRST_TIME, ft)
        editor.apply()
    }

    fun removeFirsttime() {
        sharedPreferences.edit().remove("firsttime").apply()
    }
}