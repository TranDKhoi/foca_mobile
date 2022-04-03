package com.example.foca_mobile.activity.user.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.adapterClass.AddFoodAdapter
import com.example.foca_mobile.adapterClass.RestaurantNearestAdapter
import com.example.foca_mobile.databinding.FragmentHomeBinding
import com.example.foca_mobile.model.AddFood
import com.example.foca_mobile.model.RestaurantNearest

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newArrayRestaurantList: ArrayList<RestaurantNearest>
    private lateinit var newArrayFoodList: ArrayList<AddFood>

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
        var resAdapter =  RestaurantNearestAdapter(newArrayRestaurantList)
        binding.restaurantRecyclerView.adapter = resAdapter
//        resAdapter.setOnItemClickListener(object : RestaurantNearestAdapter.ItemClickListener{
//            override fun onItemClick(position: Int) {
////                Toast.makeText(context, newArrayRestaurantList[position].txtName + newArrayRestaurantList[position].txtNumber, Toast.LENGTH_SHORT).show()
//                val intent = Intent(context,OrderFood_Activity::class.java)
//                startActivity(intent)
//            }
//        })
        binding.txtViewmorePopularmenu.setOnClickListener {
            val intent = Intent(context,OrderFood_Activity::class.java)
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
    }
    private fun createFoodRecyclerView() {
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.foodRecyclerView.setHasFixedSize(true)
        newArrayFoodList = arrayListOf<AddFood>()
        newArrayFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            AddFood(
                R.drawable.restaurant_image1,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        binding.foodRecyclerView.adapter = AddFoodAdapter(newArrayFoodList)
    }
}