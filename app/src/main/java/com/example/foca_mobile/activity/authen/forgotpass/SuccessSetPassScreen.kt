package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.databinding.ActivitySuccessSetPassScreenBinding

class SuccessSetPassScreen : AppCompatActivity() {
    private lateinit var binding:ActivitySuccessSetPassScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_set_pass_screen)

        binding.loginBtn.setOnClickListener {
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