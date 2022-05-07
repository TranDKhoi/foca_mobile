package com.example.foca_mobile.activity.user.home.orderfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserPopularMenuBinding

class PopularMenu : AppCompatActivity() {

    private lateinit var binding: ActivityUserPopularMenuBinding
    private lateinit var newArrayAddFoodList : ArrayList<Food>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityUserPopularMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createAddFoodRecyclerView()

        binding.buttonBack.setOnClickListener {
            this.finish()
        }
    }
    private fun createAddFoodRecyclerView() {
        binding.addFoodRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.addFoodRecyclerView.setHasFixedSize(true)

        newArrayAddFoodList = arrayListOf<Food>()
        newArrayAddFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayAddFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayAddFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayAddFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        binding.addFoodRecyclerView.adapter = FoodAdapter(this,newArrayAddFoodList)
    }
}