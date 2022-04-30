package com.example.foca_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.home.AdminHomeFragment
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.activity.user.chat.listmess.ListMessageFragment
import com.example.foca_mobile.activity.user.home.HomeFragment
import com.example.foca_mobile.databinding.ActivityMainBinding
import com.example.foca_mobile.model.User
import com.example.foca_mobile.utils.LoginPrefs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentUser: User

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        currentUser = intent.getSerializableExtra("currentUser") as User;


        //THIS IS WHERE WE DECIDE WHO IS LOGIN
        if (currentUser.role == "USER") {
            //USER FRAGMENT
            val userHomeFragment = HomeFragment()
            val messageFragment = ListMessageFragment()

            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(userHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> setCurrentFragment(userHomeFragment)
                    R.id.message -> setCurrentFragment(messageFragment);
                    R.id.cart -> Toast.makeText(this, currentUser.fullName, Toast.LENGTH_LONG)
                        .show()
                    R.id.user -> toLoginScreen();//sign out
                }
            }
        } else if (currentUser.role == "ADMIN") {
            //ADMIN FRAGMENT
            val adminHomeFragment = AdminHomeFragment()

            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(adminHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> setCurrentFragment(adminHomeFragment)
                    R.id.cart -> Toast.makeText(this, currentUser.role, Toast.LENGTH_LONG)
                        .show()
                    R.id.user -> toLoginScreen();//sign out
                }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userhomeFragment = UserHomeFragment()
        binding.bottomNavigation.setItemSelected(R.id.home)
        binding.bottomNavigation.showBadge(R.id.message, 2)
        setCurrentFragment(userhomeFragment)

        binding.bottomNavigation.setOnItemSelectedListener{ id ->
            when (id) {
                R.id.home -> setCurrentFragment(userhomeFragment)
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            commit()
        }

    private fun toLoginScreen() {
        LoginPrefs.removeToken();
        val it = Intent(this, LoginScreen::class.java);
        finishAffinity();

        startActivity(it);
        this.finish();
    }

}