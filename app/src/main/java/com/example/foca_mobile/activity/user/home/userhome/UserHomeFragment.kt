package com.example.foca_mobile.activity.user.home.userhome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.orderfood.PopularMenu
import com.example.foca_mobile.activity.user.home.orderfood.FoodAdapter
import com.example.foca_mobile.databinding.FragmentUserHomeBinding
import com.example.foca_mobile.activity.user.home.orderfood.Food

class UserHomeFragment : Fragment(R.layout.fragment_user_home) {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newArrayRecentFoodList: ArrayList<RecentFood>
    private lateinit var newArrayFoodList: ArrayList<Food>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserHomeBinding.inflate(
            inflater,
            container,
            false
        )
        createRestaurantRecyclerView()
        createFoodRecyclerView()
        val recentAdapter =  RecentFoodAdapter(newArrayRecentFoodList)
        binding.restaurantRecyclerView.adapter = recentAdapter
        val foodAdapter = this.context?.let { FoodAdapter(it,newArrayFoodList) }
        binding.foodRecyclerView.adapter = foodAdapter
        binding.txtViewmorePopularmenu.setOnClickListener {
            val intent = Intent(context, PopularMenu::class.java)
            startActivity(intent)
        }
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

        newArrayRecentFoodList = arrayListOf()
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20
            )
        )
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.resturant_image2,
                "Trần Thị Nhu",
                20
            )
        )
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20
            )
        )
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.resturant_image2,
                "Trần Thị Nhu",
                20
            )
        )
    }
    private fun createFoodRecyclerView() {
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.foodRecyclerView.setHasFixedSize(true)
        newArrayFoodList = arrayListOf()
        newArrayFoodList.add(
            Food(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            Food(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            Food(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            Food(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
    }
}