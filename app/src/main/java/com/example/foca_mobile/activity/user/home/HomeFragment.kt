package com.example.foca_mobile.activity.user.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.adapterClass.FoodPriceAdapter
import com.example.foca_mobile.adapterClass.RestaurantNearestAdapter
import com.example.foca_mobile.databinding.FragmentHomeBinding
import com.example.foca_mobile.model.FoodPrice
import com.example.foca_mobile.model.RestaurantNearest

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newArrayRestaurantList: ArrayList<RestaurantNearest>
    private lateinit var newArrayFoodList: ArrayList<FoodPrice>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        createRestaurantRecyclerView()
        createFoodRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun createRestaurantRecyclerView() {
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.restaurantRecyclerView.setHasFixedSize(true)

        newArrayRestaurantList = arrayListOf<RestaurantNearest>()
        newArrayRestaurantList.add(
            RestaurantNearest(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                "20" + " mins"
            )
        )
        newArrayRestaurantList.add(
            RestaurantNearest(
                R.drawable.resturant_image2,
                "Trần Thị Nhu",
                "35" + " mins"
            )
        )
        newArrayRestaurantList.add(
            RestaurantNearest(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                "20" + " mins"
            )
        )
        newArrayRestaurantList.add(
            RestaurantNearest(
                R.drawable.resturant_image2,
                "Trần Thị Nhu",
                "35" + " mins"
            )
        )
        binding.restaurantRecyclerView.adapter = RestaurantNearestAdapter(newArrayRestaurantList)
    }
    private fun createFoodRecyclerView() {
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.foodRecyclerView.setHasFixedSize(true)
        newArrayFoodList = arrayListOf<FoodPrice>()
        newArrayFoodList.add(
            FoodPrice(
                R.drawable.image_food,
                "Lê Hải Phong",
                "Nhung",
                "$" + "20"
            )
        )
        newArrayFoodList.add(
            FoodPrice(
                R.drawable.image_food,
                "Lê Hải Phong",
                "Nhung",
                "$" + "20"
            )
        )
        newArrayFoodList.add(
            FoodPrice(
                R.drawable.image_food,
                "Lê Hải Phong",
                "Nhung",
                "$" + "20"
            )
        )
        newArrayFoodList.add(
            FoodPrice(
                R.drawable.image_food,
                "Lê Hải Phong",
                "Nhung",
                "$" + "20"
            )
        )
        binding.foodRecyclerView.adapter = FoodPriceAdapter(newArrayFoodList)
    }
}