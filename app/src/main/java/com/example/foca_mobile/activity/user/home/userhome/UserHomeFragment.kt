package com.example.foca_mobile.activity.user.home.userhome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.orderfood.Food
import com.example.foca_mobile.activity.user.home.orderfood.FoodAdapter
import com.example.foca_mobile.activity.user.home.orderfood.PopularMenu
import com.example.foca_mobile.activity.user.notifi.UserNotification
import com.example.foca_mobile.databinding.FragmentUserHomeBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Notification
import com.example.foca_mobile.service.NotificationService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHomeFragment : Fragment(R.layout.fragment_user_home) {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newArrayRecentFoodList: ArrayList<RecentFood>
    private lateinit var newArrayFoodList: ArrayList<Food>

    private var isHaveNewNoti: MutableLiveData<ArrayList<Int>> = MutableLiveData()

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
        val recentAdapter = RecentFoodAdapter(newArrayRecentFoodList)
        binding.restaurantRecyclerView.adapter = recentAdapter
        val foodAdapter = this.context?.let { FoodAdapter(it, newArrayFoodList) }
        binding.foodRecyclerView.adapter = foodAdapter
        binding.txtViewmorePopularmenu.setOnClickListener {
            val intent = Intent(context, PopularMenu::class.java)
            startActivity(intent)
        }
        binding.notifyBtn.setOnClickListener {
            val intent = Intent(context, UserNotification::class.java)
            startActivity(intent)
        }

        //get not seen notify
        getUnseenNotify()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUnseenNotify()
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
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20
            )
        )
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.favorite_9,
                "Trần Thị Nhu",
                20
            )
        )
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20
            )
        )
        newArrayRecentFoodList.add(
            RecentFood(
                R.drawable.favorite_9,
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
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
        newArrayFoodList.add(
            Food(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20,
                R.drawable.ic_add
            )
        )
    }

    private fun getUnseenNotify() {
        //CALL API
        val getNotificationCall = ServiceGenerator.buildService(NotificationService::class.java)
            .getUserNotify("false")

        getNotificationCall?.enqueue(object : Callback<ApiResponse<MutableList<Notification>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Notification>>>,
                response: Response<ApiResponse<MutableList<Notification>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Notification>> = response.body()!!

                    val listNotification: ArrayList<Int> = arrayListOf()
                    for (i in 0 until res.data.size) {
                        res.data[i].id?.let { listNotification.add(it) }
                    }
                    if(listNotification.size > 0 )
                        binding.notifyBtn.setImageResource(R.drawable.ic_notification_badge)
                    else
                        binding.notifyBtn.setImageResource(R.drawable.ic_notification_non)

                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Toast.makeText(context, errorRes.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<Notification>>>,
                t: Throwable
            ) {
            }
        })
    }
}