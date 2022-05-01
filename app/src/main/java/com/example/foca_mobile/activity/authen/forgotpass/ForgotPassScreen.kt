package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen

class ForgotPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_screen)
    }

    public fun toLoginScreen(view: View) {
        this.finish();
    }

    public fun toVerifiPassScreen(view: View) {

        val id: EditText = findViewById(R.id.idfield);
        if (id.text.toString().isNullOrEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error);
            return;
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal);

        val it: Intent = Intent(this, VerifiPassScreen::class.java);
        it.putExtra("id",id.text.toString());
        startActivity(it);
    }
}