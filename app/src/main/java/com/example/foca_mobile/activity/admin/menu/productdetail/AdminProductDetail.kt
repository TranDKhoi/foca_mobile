package com.example.foca_mobile.activity.admin.menu.productdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.infofood.ReviewDetailsActivity
import com.example.foca_mobile.activity.user.home.infofood.ReviewFoodAdapter
import com.example.foca_mobile.databinding.ActivityAdminProductDetailBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.model.Review
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminProductDetail : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProductDetailBinding
    private lateinit var product: Product
    private lateinit var productDetails: ProductDetails
    private lateinit var reviewArray: MutableList<Review>

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

        createReviewRecycleview()

        binding.txtViewDetails.setOnClickListener {
            val intent = Intent(this, ReviewDetailsActivity::class.java)
            intent.putExtra("id", product.id)
            startActivity(intent)
        }
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

    private fun createReviewRecycleview() {
        binding.reviewRecycleview.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycleview.setHasFixedSize(true)
        reviewArray = arrayListOf()
        binding.progressBar.visibility = ProgressBar.VISIBLE
        val getReviewAPI =
            ServiceGenerator.buildService(ProductService::class.java)
                .getProductDetails(product.id!!)
        getReviewAPI?.enqueue(object : Callback<ApiResponse<ProductDetails>> {
            override fun onResponse(
                call: Call<ApiResponse<ProductDetails>>,
                response: Response<ApiResponse<ProductDetails>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    productDetails = res.data
                    res.data.reviews?.let { reviewArray.addAll(it) }
                    binding.reviewRecycleview.adapter = ReviewFoodAdapter(reviewArray)
                    if (reviewArray.size == 0) {
                        binding.emptyReview.visibility = ImageView.VISIBLE
                        binding.txtViewDetails.visibility = TextView.GONE
                    }
                    initValueView()
                    binding.progressBar.visibility = ProgressBar.GONE
                    binding.view1.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ApiResponse<ProductDetails>>, t: Throwable) {}
        })
    }

    private fun initValueView() {
        Glide.with(binding.root.context)
            .load(productDetails.image)
            .into(binding.productImage)
        binding.productName.text = productDetails.name
        binding.foodDes.text = productDetails.description
        if (productDetails.averageRating != null) {
            binding.rBar.rating = (productDetails.averageRating!!.toFloat())
            binding.avgRating.text =
                ((productDetails.averageRating!!.toFloat() * 100.0).roundToInt() / 100.0).toString()
        }
        binding.numOrder.text =
            productDetails.orderCount.toString().plus(" ").plus(resources.getString(R.string.Order))
    }
}