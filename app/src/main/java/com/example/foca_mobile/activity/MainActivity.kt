package com.example.foca_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.HomeFragment
import com.example.foca_mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val homeFragment = HomeFragment()
        binding.bottomNavigation.setItemSelected(R.id.home)
        binding.bottomNavigation.showBadge(R.id.message, 2)
        setCurrentFragment(homeFragment)


        binding.bottomNavigation.setOnItemSelectedListener{ id ->
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