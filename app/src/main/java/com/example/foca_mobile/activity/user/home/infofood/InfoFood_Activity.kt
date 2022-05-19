package com.example.foca_mobile.activity.user.home.infofood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserInfoFoodBinding
import com.example.foca_mobile.model.*
import com.example.foca_mobile.service.CartService
import com.example.foca_mobile.service.InterestedProductService
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt
import kotlin.properties.Delegates


class InfoFood_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoFoodBinding
    private lateinit var newArrayReviewFoodList: MutableList<Review>
    private lateinit var productDetails : ProductDetails
    private var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.view1.visibility = View.GONE
        binding.addFood.visibility = View.GONE
        initToolBar()
        binding.addFood.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("productId",productDetails.id)
            jsonObject.put("quantity",1)
            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val creatCartAPI = ServiceGenerator.buildService(CartService::class.java).createCart(requestBody)
            creatCartAPI.enqueue(object: Callback<ApiResponse<Cart>>{
                override fun onResponse(
                    call: Call<ApiResponse<Cart>>,
                    response: Response<ApiResponse<Cart>>
                ) {
                    Toast.makeText(applicationContext, "Đã thêm đồ ăn vào giỏi hàng", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {}

            })
        }
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        id = intent.getIntExtra("id",0)
        creatReviewRecycleview()
        binding.reviewRecycleview.adapter = ReviewFoodAdapter(newArrayReviewFoodList)
        binding.txtViewDetails.setOnClickListener {
            val intent = Intent(this,ReviewDetailsActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
        binding.favoriteFood.setOnClickListener {
            if(productDetails.isFavorited == true) {
                binding.favoriteFood.setImageResource(R.drawable.ic_love_defaut)
                val deleteFavoriteAPI = productDetails.id?.let { it1 ->
                    ServiceGenerator.buildService(InterestedProductService::class.java).deleteInterestedProduct(it1)
                }
                deleteFavoriteAPI?.enqueue(object : Callback<ApiResponse<Int>>{
                    override fun onResponse(
                        call: Call<ApiResponse<Int>>,
                        response: Response<ApiResponse<Int>>
                    ) {
                        Toast.makeText(applicationContext, "Đã xóa đồ ăn khỏi mục yêu thích", Toast.LENGTH_LONG).show()
                    }
                    override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {}

                })
                productDetails.isFavorited = !productDetails.isFavorited!!
            } else {
                binding.favoriteFood.setImageResource(R.drawable.ic_love)
                val jsonObject = JSONObject()
                jsonObject.put("productId",productDetails.id)
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val addFavoriteAPI = ServiceGenerator.buildService(InterestedProductService::class.java).addInterestedProduct(requestBody)
                addFavoriteAPI?.enqueue(object : Callback<ApiResponse<Int>>{
                    override fun onResponse(
                        call: Call<ApiResponse<Int>>,
                        response: Response<ApiResponse<Int>>
                    ) {
                        Toast.makeText(applicationContext, "Đã thêm đồ ăn vào mục yêu thích", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {}

                })
                productDetails.isFavorited = !productDetails.isFavorited!!
            }
        }
    }

    private fun initValueView() {
        Glide.with(binding.root.context)
            .load(productDetails.image)
            .into(binding.imageFood)
        binding.nameFood.text = productDetails.name
        binding.description.text = productDetails.description
        binding.rBar.rating = productDetails.averageRating!!.toFloat()
        binding.avgRating.text = ((productDetails.averageRating!!.toFloat() * 100.0).roundToInt() / 100.0).toString()
        binding.numOrder.text = productDetails.orderCount.toString() + "+ Order"
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    private fun creatReviewRecycleview() {
        binding.reviewRecycleview.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycleview.setHasFixedSize(true)
        newArrayReviewFoodList = arrayListOf()
        binding.progressBar.visibility = ProgressBar.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val getReviewAPI = ServiceGenerator.buildService(ProductService::class.java).getProductDetails(id)
            getReviewAPI?.enqueue(object : Callback<ApiResponse<ProductDetails>> {
                override fun onResponse(
                    call: Call<ApiResponse<ProductDetails>>,
                    response: Response<ApiResponse<ProductDetails>>
                ) {
                    val res = response.body()!!
                    if(res.data.isFavorited == true) {
                        binding.favoriteFood.setImageResource(R.drawable.ic_love)
                    } else {
                        binding.favoriteFood.setImageResource(R.drawable.ic_love_defaut)
                    }
                    productDetails = res.data
                    res.data.reviews?.let { newArrayReviewFoodList.addAll(it) }
                    binding.reviewRecycleview.adapter = ReviewFoodAdapter(newArrayReviewFoodList)
                    initValueView()
                    binding.progressBar.visibility = ProgressBar.GONE
                    binding.view1.visibility = View.VISIBLE
                    binding.addFood.visibility = View.VISIBLE
                }

                override fun onFailure(call: Call<ApiResponse<ProductDetails>>, t: Throwable) {}
            })
        }
    }
}