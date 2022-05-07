package com.example.foca_mobile.activity.user.cart_order

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrderDetail
import com.example.foca_mobile.databinding.ActivityUserOrderDetailBinding


class UserDetailOrder : AppCompatActivity() {

    private lateinit var binding: ActivityUserOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val listFood: ArrayList<Food> = intent.getSerializableExtra("listFood") as ArrayList<Food>

        val adapter = RecyclerViewAdapterOrderDetail(listFood)

        binding.rvFood.adapter = adapter
        binding.rvFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.orderDetailBack.setOnClickListener { finish() }
        binding.orderDetailButton.setOnClickListener{
            val intent = Intent(this, UserRateScreen::class.java)
            startActivity(intent)
        }
    }

}