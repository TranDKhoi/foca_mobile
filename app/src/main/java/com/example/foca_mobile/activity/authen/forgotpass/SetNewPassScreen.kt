package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen

class SetNewPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_pass_screen)
    }

    public fun toVerifiPassScreen(view: View) {

        this.finish();
    }

    public fun toSuccessScreen(view: View) {

        val it: Intent = Intent(this, SuccessSetPassScreen::class.java);
        finishAffinity();
        startActivity(it);
        this.finish();
    }
}