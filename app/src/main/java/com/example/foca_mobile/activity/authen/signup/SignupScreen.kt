package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.foca_mobile.R

class SignupScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)
    }

    public fun toLoginScreen(view: View) {
        this.finish();
    }

    public fun toVerifiScreen(view: View) {

        val id: EditText = findViewById(R.id.idfield);
        val pass: EditText = findViewById(R.id.passfield);
        val rePass: EditText = findViewById(R.id.repass);

        if (id.text.toString().isNullOrEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (pass.text.toString().isNullOrEmpty()) {
            pass.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            pass.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (rePass.text.toString().isNullOrEmpty()) {
            rePass.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            rePass.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (id.text.toString().isNullOrEmpty() ||
            pass.text.toString().isNullOrEmpty() ||
            rePass.text.toString().isNullOrEmpty()
        )
            return;

        if (pass.text.toString() != rePass.text.toString()) {
            Toast.makeText(this, "The password does not match!", Toast.LENGTH_LONG).show();
            return;
        }

        val it: Intent = Intent(this, VerifiAccountScreen::class.java);
        it.putExtra("id",id.text.toString());
        startActivity(it);
    }
}