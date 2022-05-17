package com.example.foca_mobile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.GlobalObject.setLocale
import com.example.foca_mobile.utils.LanguagePrefs
import com.example.foca_mobile.utils.NightModePrefs

class SplashScreen : AppCompatActivity() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this

        val night = NightModePrefs.getNightMode()
        if (night != "")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val lang = LanguagePrefs.getLang()
        if (lang == "")
            setLocale(this,"en")
        else
            setLocale(this,lang)

        setContentView(R.layout.activity_splash_screen)
        val mainlogo: LinearLayoutCompat = findViewById(R.id.mainlogo)

        mainlogo.alpha = 0f
        mainlogo.animate().setDuration(1500).alpha(1f).withEndAction {
            val it = Intent(this, LoginScreen::class.java)
            startActivity(it)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            this.finish()
        }
    }
}