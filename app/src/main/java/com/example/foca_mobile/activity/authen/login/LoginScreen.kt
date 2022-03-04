package com.example.foca_mobile.activity.authen.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.forgotpass.ForgotPassScreen
import com.example.foca_mobile.activity.authen.signup.SignupScreen

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
    }


    public fun toSignupScreen(view: View) {
        val intent: Intent = Intent(this, SignupScreen::class.java);

        startActivity(intent);
    }

    public fun toForgotPassScreen(view: View) {
        val intent: Intent = Intent(this, ForgotPassScreen::class.java);

        startActivity(intent);
    }
}