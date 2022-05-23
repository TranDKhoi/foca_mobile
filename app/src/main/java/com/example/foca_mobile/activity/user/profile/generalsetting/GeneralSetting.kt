package com.example.foca_mobile.activity.user.profile.generalsetting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.SplashScreen
import com.example.foca_mobile.databinding.ActivityGeneralSettingBinding
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.LanguagePrefs
import com.example.foca_mobile.utils.NightModePrefs
import com.example.foca_mobile.utils.NotifyLevelPrefs


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

        //SET THE SWITCH BUTTON
        setSwitchButton()

        binding.switchBtn.setOnClickListener {
            changeNightMode()
        }
        binding.switchMess.setOnClickListener {
            if (binding.switchMess.isChecked) {
                NotifyLevelPrefs.setLevel1("")
            } else {
                NotifyLevelPrefs.setLevel1("low")
            }
        }
        binding.switchOrder.setOnClickListener {
            if (binding.switchOrder.isChecked) {
                NotifyLevelPrefs.setLevel2("")
            } else {
                NotifyLevelPrefs.setLevel2("low")
            }
        }
    }

    private fun setSwitchButton() {
        val night = NightModePrefs.getNightMode()
        binding.switchBtn.isChecked = night != ""

        val lv1 = NotifyLevelPrefs.getLevel1()
        binding.switchMess.isChecked = lv1 == ""

        val lv2 = NotifyLevelPrefs.getLevel2()
        binding.switchOrder.isChecked = lv2 == ""
    }

    private fun changeNightMode() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.Warning))
        builder.setMessage(resources.getString(R.string.Restartapp))
        builder.setPositiveButton(resources.getString(R.string.YES)) { dialog, which ->
            if (binding.switchBtn.isChecked) {
                NightModePrefs.setNightMode("night")
                val i =
                    baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                finishAffinity()
            } else {
                NightModePrefs.setNightMode("")
                val i =
                    baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                finishAffinity()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(
            resources.getString(R.string.NO)
        ) { dialog, which -> // Do nothing
            dialog.dismiss()
            setSwitchButton()
        }
        val alert = builder.create()

        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }

        alert.show()
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
                    recreate()
                }
                1 -> {
                    GlobalObject.setLocale(SplashScreen.appContext, "vi")
                    LanguagePrefs.setLang("vi")
                    recreate()
                }
            }
            GlobalObject.isChangeLanguage = true
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()

    }
}