package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.foca_mobile.R

class VerifiAccountScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifi_account_screen)

        val resString: String = intent.getStringExtra("id").toString();

        val subText: TextView = findViewById(R.id.subText);
        subText.text = "Code send to $resString@gm.uit.edu.vn";
    }

    public fun toSignupScreen(view: View) {
        this.finish();
    }

    public fun toUploadBioScreen(view: View) {

        val code: EditText = findViewById(R.id.codefield);
        if (code.text.toString().isNullOrEmpty() || code.text.toString().length < 4) {
            code.setBackgroundResource(R.drawable.rounded_edittext_error);
            return;
        } else
            code.setBackgroundResource(R.drawable.rounded_edittext_normal);

        val it: Intent = Intent(this, BioScreen::class.java);
        finishAffinity();
        startActivity(it);
        this.finish();
    }

}