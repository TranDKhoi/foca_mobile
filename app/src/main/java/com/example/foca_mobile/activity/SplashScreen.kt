package com.example.foca_mobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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