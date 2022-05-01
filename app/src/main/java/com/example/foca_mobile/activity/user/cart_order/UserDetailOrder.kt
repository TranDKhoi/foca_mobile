package com.example.foca_mobile.activity.user.cart_order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrderDetail
import com.example.foca_mobile.databinding.ActivityUserDetailOrderBinding


class UserDetailOrder : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val listFood: ArrayList<Food> = intent.getSerializableExtra("listFood") as ArrayList<Food>

        val adapter = RecyclerViewAdapterOrderDetail(listFood)

        binding.rvFood.adapter = adapter
        binding.rvFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.orderDetailBack.setOnClickListener { finish() }
    }

}