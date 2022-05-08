package com.example.foca_mobile.activity.admin.menu.productdetail

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ActivityAdminProductDetailBinding
import com.example.foca_mobile.model.Product
import com.google.gson.Gson

class AdminProductDetail : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProductDetailBinding
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()

        val res = intent.getStringExtra("product")
        product = Gson().fromJson(res, Product::class.java)

        binding.productName.text = product.name
        Glide.with(applicationContext)
            .load(product.image)
            .into(binding.productImage)
        binding.foodDes.text = product.description
        binding.emptyReview.visibility = ImageView.VISIBLE
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
    }
}