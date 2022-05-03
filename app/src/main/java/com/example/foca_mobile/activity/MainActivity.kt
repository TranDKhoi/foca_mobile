package com.example.foca_mobile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.home.AdminHomeFragment
import com.example.foca_mobile.activity.admin.order.AdminOrderManagement
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.activity.user.cart_order.UserMyCart
import com.example.foca_mobile.activity.user.chat.listmess.ListMessageFragment
import com.example.foca_mobile.activity.user.home.userhome.UserHomeFragment
import com.example.foca_mobile.activity.user.profile.UserProfileFragment
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
            val messageFragment = ListMessageFragment() // sửa lại
            val cartFragment = UserMyCart()
            val profileFragment = UserProfileFragment()

            binding.bottomNavigation.setMenuResource(R.menu.user_menu)
            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(userHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> setCurrentFragment(userHomeFragment)
                    R.id.message -> setCurrentFragment(messageFragment);
                    R.id.cart -> setCurrentFragment(cartFragment)
                    R.id.user -> setCurrentFragment(profileFragment)
                }
            }
        } else if (currentUser.role == "ADMIN") {
            //ADMIN FRAGMENT
            val adminHomeFragment = AdminHomeFragment()
            val messageFragment = ListMessageFragment()
            val orderManage = AdminOrderManagement()
            val profileFragment = UserProfileFragment()

            binding.bottomNavigation.setMenuResource(R.menu.admin_menu)
            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(adminHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> setCurrentFragment(adminHomeFragment)
                    R.id.message -> setCurrentFragment(messageFragment)
                    R.id.cart -> toOrderManagementScreen()
                    R.id.user -> setCurrentFragment(profileFragment)
                }
            }
        }
    }

    private fun toOrderManagementScreen() {

        binding.bottomNavigation.setItemSelected(R.id.cart)
        val it = Intent(this, AdminOrderManagement::class.java)
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

    //HIDE KEYBOARD WHEN LOST FOCUS
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this!!.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}