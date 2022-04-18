package com.example.foca_mobile.activity.user.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityInfoFoodBinding
import com.example.foca_mobile.model.ReviewFood
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InfoFood_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoFoodBinding
    private lateinit var newArrayReviewFoodList: ArrayList<ReviewFood>
    var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityInfoFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reviewRecycleview.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycleview.setHasFixedSize(true)
        newArrayReviewFoodList = arrayListOf<ReviewFood>()
//        newArrayReviewFoodList.add(
//            ReviewFood(
//                R.drawable.photo_profile_1,
//                "Dianne Russell",
//                LocalDate.parse("2018-12-12"),
//                R.drawable.ic_add
//            )
//        )
//        newArrayReviewFoodList.add(
//            ReviewFood(
//                R.drawable.photo_profile_2,
//                "Lê Hải Phong",
//                20,
//                R.drawable.ic_add
//            )
//        )
    }
}