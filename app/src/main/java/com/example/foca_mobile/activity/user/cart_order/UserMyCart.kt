package com.example.foca_mobile.activity.user.cart_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.adapter.CartOrderAdapter
import com.example.foca_mobile.databinding.ActivityUserMyCartBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserMyCart : AppCompatActivity() {

    private var tabTitle = arrayOf("My cart","My orders")
    private  lateinit var  binding: ActivityUserMyCartBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.myCartViewPager.adapter = CartOrderAdapter(supportFragmentManager, lifecycle)
        binding.myCartViewPager.isUserInputEnabled=false

        TabLayoutMediator(binding.myCartTabLayout, binding.myCartViewPager){
            tab, position ->
                tab.text = tabTitle[position]

        }.attach()

    }
}