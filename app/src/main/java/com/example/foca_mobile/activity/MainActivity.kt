package com.example.foca_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.home.AdminHomeFragment
import com.example.foca_mobile.activity.admin.order.OrderManagement
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.activity.user.cart_order.UserMyCart
import com.example.foca_mobile.activity.user.chat.listmess.ListMessageFragment
import com.example.foca_mobile.activity.user.home.userhome.UserHomeFragment
import com.example.foca_mobile.databinding.ActivityMainBinding
import com.example.foca_mobile.model.User
import com.example.foca_mobile.utils.LoginPrefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        currentUser = intent.getSerializableExtra("currentUser") as User;


        //THIS IS WHERE WE DECIDE WHO IS LOGIN
        if (currentUser.role == "USER") {
            //USER FRAGMENT
            val userHomeFragment = UserHomeFragment()
            val messageFragment = ListMessageFragment()
            val cartFragment = UserMyCart()

            binding.bottomNavigation.setMenuResource(R.menu.user_menu)
            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(userHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> setCurrentFragment(AdminHomeFragment())
                    R.id.message -> setCurrentFragment(messageFragment);
                    R.id.cart -> setCurrentFragment(cartFragment)
                    R.id.user -> toLoginScreen();//sign out
                }
            }
        } else if (currentUser.role == "ADMIN") {
            //ADMIN FRAGMENT
            val adminHomeFragment = AdminHomeFragment()
            val orderManage = OrderManagement()

            binding.bottomNavigation.setMenuResource(R.menu.admin_menu)
            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(adminHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> setCurrentFragment(adminHomeFragment)
                    R.id.cart -> toOrderManagementScreen()
                    R.id.user -> toLoginScreen();//sign out
                }
            }
        }
    }

    private fun toOrderManagementScreen() {

        binding.bottomNavigation.setItemSelected(R.id.home)
        val it = Intent(this, OrderManagement::class.java)
        startActivity(it)
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