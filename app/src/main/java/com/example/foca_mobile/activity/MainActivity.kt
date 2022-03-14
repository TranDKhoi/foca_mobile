package com.example.foca_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()

        bottom_navigation.setItemSelected(R.id.home)
        bottom_navigation.showBadge(R.id.message,2)
        setCurrentFragment(homeFragment)


        bottom_navigation.setOnItemSelectedListener{ id ->
            when (id) {
                R.id.home -> setCurrentFragment(homeFragment)
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            commit()
        }
}