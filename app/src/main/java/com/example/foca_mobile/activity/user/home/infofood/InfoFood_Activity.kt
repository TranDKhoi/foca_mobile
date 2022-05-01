package com.example.foca_mobile.activity.user.home.infofood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityInfoFoodBinding
import java.time.LocalDate
import kotlin.collections.ArrayList

class InfoFood_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoFoodBinding
    private lateinit var newArrayReviewFoodList: ArrayList<ReviewFood>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        binding.toolbar.setNavigationOnClickListener{
            this.finish()
        }
        creatReviewRecycleview()
        binding.reviewRecycleview.adapter = ReviewFoodAdapter(newArrayReviewFoodList)
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
        binding.reviewRecycleview.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycleview.setHasFixedSize(true)
        newArrayReviewFoodList = arrayListOf<ReviewFood>()
        for (i in 0..5) {
            newArrayReviewFoodList.add(
                ReviewFood(
                    R.drawable.photo_profile_1,
                    "Dianne Russell",
                    LocalDate.parse("2022-04-18"),
                    "This Is great, So delicious! You Must Here, With Your family . . ",
                    5
                )
            )
            newArrayReviewFoodList.add(
                ReviewFood(
                    R.drawable.photo_profile_2,
                    "Dianne Russell",
                    LocalDate.parse("2022-04-18"),
                    "This Is great, So delicious! You Must Here, With Your family . . ",
                    5
                )
            )
        }
    }
}