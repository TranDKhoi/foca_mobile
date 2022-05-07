package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.activity.MainActivity
import com.example.foca_mobile.databinding.ActivitySuccessCreateAccountScreenBinding
import com.example.foca_mobile.model.User
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.LoginPrefs

class SuccessCreateAccountScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessCreateAccountScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessCreateAccountScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.leteatBtn.setOnClickListener {
            toUserHomeScreen(GlobalObject.CurrentUser)
        }
    }

    private fun toUserHomeScreen(user: User) {

        LoginPrefs.setUserToken(user.accessToken!!)
        LoginPrefs.setUser(user)

        val it = Intent(this, MainActivity::class.java)
        SocketHandler.initSocket(user.id!!)
        GlobalObject.CurrentUser = user
        finishAffinity()
        startActivity(it)
        this.finish()
    }
}
