package com.example.foca_mobile.activity.user.profile.profilesetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserProfileBinding
import com.bumptech.glide.Glide
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.InterestedProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.GlobalObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var newArrayFavoriteFoodList: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        creatReviewRecycleview()
        binding.favoriteRecycleview.adapter = FavoriteAdapter(applicationContext,newArrayFavoriteFoodList)

        Glide.with(applicationContext)
            .load(GlobalObject.CurrentUser.photoUrl)
            .into(binding.userImage)
        binding.nameProfile.text = GlobalObject.CurrentUser.fullName
        binding.emailProfile.text = GlobalObject.CurrentUser.email
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
    }

    private fun creatReviewRecycleview() {
        binding.favoriteRecycleview.layoutManager = LinearLayoutManager(this)
        binding.favoriteRecycleview.setHasFixedSize(true)
        newArrayFavoriteFoodList = mutableListOf()
        GlobalScope.launch(Dispatchers.IO) {
            val getAllInterestedProductAPI = ServiceGenerator
                .buildService(InterestedProductService::class.java)
                .getAllInterestedProduct()
            getAllInterestedProductAPI?.enqueue(object : Callback<ApiResponse<MutableList<Product>>>{
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<Product>>>,
                    response: Response<ApiResponse<MutableList<Product>>>
                ) {
                    val res = response.body()!!.data
                    newArrayFavoriteFoodList = res
                    binding.favoriteRecycleview.adapter = FavoriteAdapter(applicationContext,newArrayFavoriteFoodList)
                }

                override fun onFailure(call: Call<ApiResponse<MutableList<Product>>>,t: Throwable) {}

            })
        }
    }
}