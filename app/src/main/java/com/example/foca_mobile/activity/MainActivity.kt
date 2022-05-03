package com.example.foca_mobile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.chat.listmess.Conversation
import com.example.foca_mobile.activity.admin.chat.listmess.ListConversationFragment
import com.example.foca_mobile.activity.admin.home.AdminHomeFragment
import com.example.foca_mobile.activity.admin.order.AdminOrderManagement
import com.example.foca_mobile.activity.user.cart_order.UserMyCart
import com.example.foca_mobile.activity.user.chat.UserChatScreen
import com.example.foca_mobile.activity.user.home.userhome.UserHomeFragment
import com.example.foca_mobile.activity.user.profile.UserProfileFragment
import com.example.foca_mobile.databinding.ActivityMainBinding
import com.example.foca_mobile.model.User
import com.example.foca_mobile.socket.SocketHandler
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentUser: User
    private var currentSelectedPage = R.id.home

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
            val cartFragment = UserMyCart()
            val profileFragment = UserProfileFragment()

            binding.bottomNavigation.setMenuResource(R.menu.user_menu)
            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(userHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> {
                        setCurrentFragment(userHomeFragment)
                        currentSelectedPage = R.id.home
                    }
                    R.id.message -> {
                        userToAdminChat()
                        binding.bottomNavigation.setItemSelected(currentSelectedPage)
                    }
                    R.id.cart -> {
                        setCurrentFragment(cartFragment)
                        currentSelectedPage = R.id.cart
                    }
                    R.id.user -> {
                        setCurrentFragment(profileFragment)
                        currentSelectedPage = R.id.user
                    }
                }
            }
        } else if (currentUser.role == "ADMIN") {
            //ADMIN FRAGMENT
            val adminHomeFragment = AdminHomeFragment()
            val messageFragment = ListConversationFragment()
            val profileFragment = UserProfileFragment()

            binding.bottomNavigation.setMenuResource(R.menu.admin_menu)
            binding.bottomNavigation.setItemSelected(R.id.home)
            binding.bottomNavigation.showBadge(R.id.message, 1)
            setCurrentFragment(adminHomeFragment)

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> {
                        setCurrentFragment(adminHomeFragment)
                        currentSelectedPage = R.id.home
                    }
                    R.id.message -> {
                        setCurrentFragment(messageFragment)
                        currentSelectedPage = R.id.message
                    }
                    R.id.cart -> {
                        toOrderManagementScreen()
                        binding.bottomNavigation.setItemSelected(currentSelectedPage)
                    }
                    R.id.user -> {
                        setCurrentFragment(profileFragment)
                        currentSelectedPage = R.id.user
                    }
                }
            }
        }
    }

    private fun userToAdminChat() {
        val intent = Intent(this, UserChatScreen::class.java)
        startActivity(intent)
    }

    private fun toOrderManagementScreen() {
        val it = Intent(this, AdminOrderManagement::class.java)
        startActivity(it)
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            commit()
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