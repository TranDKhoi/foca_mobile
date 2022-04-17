package com.example.foca_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.Utils.LoginPrefs
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.activity.user.chat.listmess.ListMessageFragment
import com.example.foca_mobile.activity.user.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        //FRAGMENT
        val homeFragment = HomeFragment()
        val messageFragment = ListMessageFragment()

        bottom_navigation.setItemSelected(R.id.home)
        bottom_navigation.showBadge(R.id.message, 1)
        setCurrentFragment(homeFragment)


        bottom_navigation.setOnItemSelectedListener { id ->
            when (id) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.message -> setCurrentFragment(messageFragment);
                R.id.cart -> Handler().postDelayed({
                    //createNotificationChannel();
                }, 2000)
                R.id.user -> toLoginScreen();//sign out
            }
        }
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            commit()
        }

    private fun toLoginScreen() {

        LoginPrefs.removeID();
        val it: Intent = Intent(this, LoginScreen::class.java);
        finishAffinity();

        startActivity(it);
        this.finish();
    }

}