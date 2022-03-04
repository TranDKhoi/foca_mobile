package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import java.nio.channels.InterruptedByTimeoutException

class VerifiPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifi_pass_screen)
    }

    public fun toForgotPassScreen(view: View) {
        this.finish();
    }

    public fun toSetNewPassScreen(view: View) {

        val it: Intent = Intent(this, SetNewPassScreen::class.java);
        startActivity(it);
    }
}