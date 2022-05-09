package com.example.foca_mobile.activity.user.profile.generalsetting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.databinding.ActivityGeneralSettingBinding
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.NightModePrefs

class GeneralSetting : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityGeneralSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.languageBtn.setOnClickListener {
            buildLanguageDialog()
        }

        binding.buttonBack.setOnClickListener {
            this.finish()
        }


        val night = NightModePrefs.getNightMode()
        if (night != "")
            binding.switchBtn.isChecked = true

        binding.switchBtn.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                recreate()
                NightModePrefs.setNightMode("night")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                recreate()
                NightModePrefs.setNightMode("")
            }
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