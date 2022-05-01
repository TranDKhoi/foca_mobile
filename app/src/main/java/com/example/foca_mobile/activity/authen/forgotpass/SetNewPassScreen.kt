package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
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

        val newPass: EditText = findViewById(R.id.newpass);
        val rePass: EditText = findViewById(R.id.repass);

        if (newPass.text.toString().isNullOrEmpty()) {
            newPass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            newPass.setBackgroundResource(R.drawable.rounded_edittext_normal)
        if (rePass.text.toString().isNullOrEmpty()) {
            rePass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            rePass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (newPass.text.toString().isNullOrEmpty() || rePass.text.toString().isNullOrEmpty())
            return;

        if (newPass.text.toString() != rePass.text.toString()) {
            Toast.makeText(this, "The password does not match!", Toast.LENGTH_LONG).show();
            return;
        }

        val it: Intent = Intent(this, SuccessSetPassScreen::class.java);
        finishAffinity();
        startActivity(it);
        this.finish();
    }
}