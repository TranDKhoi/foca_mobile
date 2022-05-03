package com.example.foca_mobile.activity.admin.menu.productdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.databinding.ActivityAdminProductDetailBinding

class AdminProductDetail : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}