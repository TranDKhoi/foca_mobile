package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import kotlinx.android.synthetic.main.activity_success_set_pass_screen.*

class SuccessSetPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_set_pass_screen)

        loginBtn.setOnClickListener {
            toLoginScreen()
        }
    }

    private fun toLoginScreen() {

        val it = Intent(this, LoginScreen::class.java)
        finishAffinity()
        startActivity(it)
        this.finish()
    }
}