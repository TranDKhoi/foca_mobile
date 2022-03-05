package com.example.foca_mobile.activity.authen.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.text.isDigitsOnly
import com.example.foca_mobile.R
import com.example.foca_mobile.Utils.LoginPrefs
import com.example.foca_mobile.activity.MainActivity
import com.example.foca_mobile.activity.authen.forgotpass.ForgotPassScreen
import com.example.foca_mobile.activity.authen.signup.SignupScreen


class LoginScreen : AppCompatActivity() {

    companion object {
        lateinit var appContext: Context;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        appContext = this;

        val id = LoginPrefs.getUserID();
        if (id != "")
            directLogin();
    }


    public fun toSignupScreen(view: View) {
        val intent: Intent = Intent(this, SignupScreen::class.java);

        startActivity(intent);
    }

    public fun toForgotPassScreen(view: View) {
        val intent: Intent = Intent(this, ForgotPassScreen::class.java);

        startActivity(intent);
    }

    public fun loginFunc(view: View) {
        val id: EditText = findViewById(R.id.idfield);
        val pas: EditText = findViewById(R.id.passfield);

        if (id.text.toString().isNullOrEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (pas.text.toString().isNullOrEmpty()) {
            pas.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            pas.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (id.text.toString().isNullOrEmpty() || pas.text.toString().isNullOrEmpty())
            return;

        LoginPrefs.setUserID("123");

        val it: Intent = Intent(this, MainActivity::class.java);
        startActivity(it);
        this.finish();
    }

    public fun directLogin() {
        val it: Intent = Intent(this, MainActivity::class.java);
        startActivity(it);
        this.finish();
    }
}