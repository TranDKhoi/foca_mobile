package com.example.foca_mobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import com.example.foca_mobile.Utils.LoginPrefs
import com.example.foca_mobile.activity.authen.login.LoginScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun toLoginScreen(view: View) {

        LoginPrefs.removeID();
        val it: Intent = Intent(this, LoginScreen::class.java);
        finishAffinity();

        startActivity(it);
        this.finish();
    }
}