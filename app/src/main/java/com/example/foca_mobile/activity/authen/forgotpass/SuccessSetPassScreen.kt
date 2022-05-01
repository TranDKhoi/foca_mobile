package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen

class SuccessSetPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_set_pass_screen)
    }

    public fun toLoginScreen(view: View) {

        val it: Intent = Intent(this, LoginScreen::class.java);
        finishAffinity();
        startActivity(it);
        this.finish();
    }
}