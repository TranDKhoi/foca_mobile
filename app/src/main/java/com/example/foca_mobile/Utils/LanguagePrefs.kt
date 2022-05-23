package com.example.foca_mobile.utils

import androidx.preference.PreferenceManager
import com.example.foca_mobile.activity.SplashScreen


object LanguagePrefs {

    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(SplashScreen.appContext)

    const val LANG = "language"

    fun getLang(): String {
        return sharedPreferences.getString(LANG, "").toString()
    }

    fun setLang(lang: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LANG, lang)
        editor.apply()
    }

    fun removeLang() {
        sharedPreferences.edit().remove("language").apply()
    }
}