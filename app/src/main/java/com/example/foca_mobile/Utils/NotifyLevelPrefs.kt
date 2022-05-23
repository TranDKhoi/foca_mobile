package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.SplashScreen

object NotifyLevelPrefs {
    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(SplashScreen.appContext)

    const val LEVEL1 = "level1"
    const val LEVEL2 = "level2"

    fun getLevel1(): String {
        return sharedPreferences.getString(LEVEL1, "").toString()
    }

    fun setLevel1(ft: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LEVEL1, ft)
        editor.apply()
    }
    fun getLevel2(): String {
        return sharedPreferences.getString(LEVEL2, "").toString()
    }

    fun setLevel2(ft: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LEVEL2, ft)
        editor.apply()
    }
    fun removeLevel1() {
        sharedPreferences.edit().remove("level1").apply()
    }
    fun removeLevel2() {
        sharedPreferences.edit().remove("level2").apply()
    }
}