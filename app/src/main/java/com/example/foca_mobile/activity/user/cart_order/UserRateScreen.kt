package com.example.foca_mobile.activity.user.cart_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foca_mobile.databinding.ActivityUserRateScreenBinding

class UserRateScreen : AppCompatActivity() {

    private lateinit var binding: ActivityUserRateScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ratingBar.stepSize = 1f
        binding.btnSkip.setOnClickListener {
            finish()
        }

    }
}