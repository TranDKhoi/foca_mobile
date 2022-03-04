package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.signup.bio.BioScreen

class VerifiScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifi_screen)
    }

    public fun toSignupScreen(view: View) {
        this.finish();
    }

    public fun toUploadBioScreen(view: View) {
        val it: Intent = Intent(this, BioScreen::class.java);
        finishAffinity();
        startActivity(it);
        this.finish();
    }

}