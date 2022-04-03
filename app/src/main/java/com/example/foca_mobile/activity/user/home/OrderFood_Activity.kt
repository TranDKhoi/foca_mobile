package com.example.foca_mobile.activity.user.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.adapterClass.AddFoodAdapter
import com.example.foca_mobile.databinding.ActivityOrderFoodBinding
import com.example.foca_mobile.model.AddFood

class OrderFood_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderFoodBinding
    private lateinit var newArrayAddFoodList : ArrayList<AddFood>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityOrderFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createAddFoodRecyclerView()
    }
    private fun createAddFoodRecyclerView() {
        binding.addFoodRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.addFoodRecyclerView.setHasFixedSize(true)

        newArrayAddFoodList = arrayListOf<AddFood>()
        newArrayAddFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayAddFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayAddFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayAddFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        binding.addFoodRecyclerView.adapter = AddFoodAdapter(newArrayAddFoodList)
    }
}