package com.example.foca_mobile.activity.user.profile.generalsetting

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.databinding.ActivityGeneralSettingBinding
import com.example.foca_mobile.utils.GlobalObject

class GeneralSetting : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.languageBtn.setOnClickListener {
            buildLanguageDialog()
        }

        binding.buttonBack.setOnClickListener {
            this.finish()
        }

    }

    private fun buildLanguageDialog() {
        // setup the alert builder
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle(resources.getString(R.string.Chooseyourlanguage))

        // add a list
        val status = arrayOf("English", "Tiếng Việt")
        builder.setItems(status) { _, which ->
            when (which) {
                0 -> {
                    GlobalObject.setLocale(LoginScreen.appContext, "en")
                    finish()
                    startActivity(intent)
                }

                1 -> {
                    GlobalObject.setLocale(LoginScreen.appContext, "vi")
                    finish()
                    startActivity(intent)
                }

            }
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()

    }
}