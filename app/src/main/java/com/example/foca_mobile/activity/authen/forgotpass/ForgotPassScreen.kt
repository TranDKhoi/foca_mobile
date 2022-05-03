package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import kotlinx.android.synthetic.main.activity_forgot_pass_screen.*

class ForgotPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_screen)

        backBtn.setOnClickListener {
            this.finish()
        }
        sendBtn.setOnClickListener {
            toVerifyPassScreen()
        }
    }

    private fun toVerifyPassScreen() {

        val id: EditText = findViewById(R.id.idfield)
        if (id.text.toString().isEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error)
            return
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal)

        val it = Intent(this, VerifiPassScreen::class.java)
        it.putExtra("id", id.text.toString())
        startActivity(it)
    }
}