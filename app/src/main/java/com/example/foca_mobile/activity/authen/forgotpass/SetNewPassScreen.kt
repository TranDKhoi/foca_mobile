package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import kotlinx.android.synthetic.main.activity_set_new_pass_screen.*

class SetNewPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_pass_screen)

        backBtn.setOnClickListener {
            this.finish()
        }
        finishbtn.setOnClickListener {
            toSuccessScreen()
        }
    }

    private fun toSuccessScreen() {

        val newPass: EditText = findViewById(R.id.newpass)
        val rePass: EditText = findViewById(R.id.repass)

        if (newPass.text.toString().isEmpty()) {
            newPass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            newPass.setBackgroundResource(R.drawable.rounded_edittext_normal)
        if (rePass.text.toString().isEmpty()) {
            rePass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            rePass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (newPass.text.toString().isEmpty() || rePass.text.toString().isEmpty())
            return

        if (newPass.text.toString() != rePass.text.toString()) {
            Toast.makeText(this, "The password does not match!", Toast.LENGTH_LONG).show()
            return
        }

        val it = Intent(this, SuccessSetPassScreen::class.java)
        finishAffinity()
        startActivity(it)
        this.finish()
    }
}