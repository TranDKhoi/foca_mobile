package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import kotlinx.android.synthetic.main.activity_verifi_pass_screen.*

class VerifiPassScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifi_pass_screen)

        val resString: String = intent.getStringExtra("id").toString()

        val subText: TextView = findViewById(R.id.subText)
        subText.text =
            resources.getString(R.string.Codesendto).plus(resString)
                .plus(resources.getString(R.string.gm_uit_edu_vn))


        backBtn.setOnClickListener {
            this.finish()
        }
        confirm_button.setOnClickListener {
            toSetNewPassScreen()
        }
    }

    private fun toSetNewPassScreen() {

        val code: EditText = findViewById(R.id.codefield)
        if (code.text.toString().isEmpty() || code.text.toString().length < 4) {
            code.setBackgroundResource(R.drawable.rounded_edittext_error)
            return
        } else
            code.setBackgroundResource(R.drawable.rounded_edittext_normal)

        val it = Intent(this, SetNewPassScreen::class.java)
        startActivity(it)
    }
}