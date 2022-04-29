package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.MainActivity
import com.example.foca_mobile.databinding.ActivityBioScreenBinding
import com.example.foca_mobile.databinding.ActivitySuccessCreateAccountScreenBinding
import com.example.foca_mobile.model.User
import com.example.foca_mobile.utils.LoginPrefs

class SuccessCreateAccountScreen : AppCompatActivity() {

    private lateinit var currentUser: User
    private lateinit var binding: ActivitySuccessCreateAccountScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessCreateAccountScreenBinding.inflate(layoutInflater);
        setContentView(binding.root)

        currentUser = intent.getSerializableExtra("currentUser") as User;

        binding.leteatBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                toUserHomeScreen(currentUser);
            }

        });
    }

    private fun toUserHomeScreen(user: User) {

        LoginPrefs.setUserToken(currentUser.accessToken!!);

        val it = Intent(this, MainActivity::class.java)
        it.putExtra("currentUser", currentUser);
        finishAffinity()
        startActivity(it)
        this.finish();
    }
}