package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.foca_mobile.R
import java.nio.channels.InterruptedByTimeoutException

class VerifiPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifi_pass_screen)

        val resString: String = intent.getStringExtra("id").toString();

        val subText: TextView = findViewById(R.id.subText);
        subText.text = "Code send to $resString@gm.uit.edu.vn";

    }

    public fun toForgotPassScreen(view: View) {
        this.finish();
    }

    public fun toSetNewPassScreen(view: View) {

        val code: EditText = findViewById(R.id.codefield);
        if (code.text.toString().isNullOrEmpty() || code.text.toString().length < 4) {
            code.setBackgroundResource(R.drawable.rounded_edittext_error);
            return;
        }else
            code.setBackgroundResource(R.drawable.rounded_edittext_normal);

        val it: Intent = Intent(this, SetNewPassScreen::class.java);
        startActivity(it);
    }
}