package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R

class SignupScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)
    }

    public fun toLoginScreen(view: View) {
        this.finish();
    }

    public fun toVerifiScreen(view: View) {
        val it: Intent = Intent(this, VerifiAccountScreen::class.java);
        startActivity(it);
    }
}