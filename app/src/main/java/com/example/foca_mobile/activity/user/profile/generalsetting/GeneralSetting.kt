package com.example.foca_mobile.activity.user.profile.generalsetting

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.SplashScreen
import com.example.foca_mobile.databinding.ActivityGeneralSettingBinding
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.LanguagePrefs
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

        binding.switchBtn.setOnCheckedChangeListener { _, b ->
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
                    GlobalObject.setLocale(SplashScreen.appContext, "en")
                    LanguagePrefs.setLang("en")
                    this.finish()
                    startActivity(intent)
                }
                1 -> {
                    GlobalObject.setLocale(SplashScreen.appContext, "vi")
                    LanguagePrefs.setLang("vi")
                    this.finish()
                    startActivity(intent)
                }
            }
            GlobalObject.isChangeLanguage = true
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()

    }
}