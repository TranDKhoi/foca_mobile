package com.example.foca_mobile.activity.authen.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.MainActivity
import com.example.foca_mobile.activity.authen.forgotpass.ForgotPassScreen
import com.example.foca_mobile.activity.authen.signup.SignupScreen
import com.example.foca_mobile.utils.LoginPrefs


class LoginScreen : AppCompatActivity() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        appContext = this

        val id = LoginPrefs.getUserID();
        if (id != "")
            directLogin();
    }


    public fun toSignupScreen(view: View) {
        val intent = Intent(this, SignupScreen::class.java);

        startActivity(intent);
    }

    public fun toForgotPassScreen(view: View) {
        val intent = Intent(this, ForgotPassScreen::class.java);

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

        val it = Intent(this, MainActivity::class.java);
        startActivity(it);
        this.finish();
    }

    public fun directLogin() {
        val it = Intent(this, MainActivity::class.java);
        startActivity(it);
        this.finish();
    }
}