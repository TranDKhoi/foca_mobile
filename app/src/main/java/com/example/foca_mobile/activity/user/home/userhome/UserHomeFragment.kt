package com.example.foca_mobile.activity.user.home.userhome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.orderfood.FoodAdapter
import com.example.foca_mobile.activity.user.home.orderfood.PopularMenu
import com.example.foca_mobile.activity.user.notifi.UserNotification
import com.example.foca_mobile.databinding.FragmentUserHomeBinding
import com.example.foca_mobile.model.*
import com.example.foca_mobile.service.NotificationService
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.ErrorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserHomeFragment : Fragment(R.layout.fragment_user_home) {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var newArrayRecentFoodList: MutableList<Product?>
    private lateinit var newArrayFoodList: MutableList<Product>

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
        createRecentFoodRecyclerView()
        createFoodRecyclerView()
        getUnseenNotify()

        binding.txtviewmoreRecentFood.setOnClickListener {}
        binding.txtViewmorePopularmenu.setOnClickListener {
            val intent = Intent(context, PopularMenu::class.java)
            startActivity(intent)
        }

        binding.notifyBtn.setOnClickListener {
            val intent = Intent(context, UserNotification::class.java)
            startActivity(intent)
        }

        //update badge notification
        val socket = SocketHandler.getSocket()
        socket.on("received_notification") {
            activity?.runOnUiThread {
                binding.notifyBtn.setImageResource(R.drawable.ic_notification_badge)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun createRecentFoodRecyclerView() {
        binding.recentFoodRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recentFoodRecyclerView.setHasFixedSize(true)
        newArrayRecentFoodList = mutableListOf()
        binding.progressBar1.visibility = ProgressBar.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val recentFoodApi = ServiceGenerator.buildService(OrderService::class.java).getRecentOrderList()
            recentFoodApi?.enqueue(object : Callback<ApiResponse<MutableList<Order>>> {
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<Order>>>,
                    response: Response<ApiResponse<MutableList<Order>>>
                ) {
                    val res = response.body()!!
                    val tempOrderDetailsList1 : MutableList<OrderDetails> = mutableListOf()
                    res.data.forEach {
                        it.orderDetails?.let { it1 -> tempOrderDetailsList1.addAll(it1) }
                    }
                    for (it in tempOrderDetailsList1) {
                        if(newArrayRecentFoodList.size == 5) break
                        if (!newArrayRecentFoodList.any { pd -> pd?.id == it.productId }) newArrayRecentFoodList.add(it.product)
                    }
                    binding.recentFoodRecyclerView.adapter = context?.let { RecentFoodAdapter(it,newArrayRecentFoodList) }
                    binding.progressBar1.visibility = ProgressBar.GONE
                }

                override fun onFailure(call: Call<ApiResponse<MutableList<Order>>>, t: Throwable) {}
            })
        }
    }
    private fun createFoodRecyclerView() {
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.foodRecyclerView.setHasFixedSize(true)
        newArrayFoodList = mutableListOf()
        binding.progressBar2.visibility = ProgressBar.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val productApi = ServiceGenerator.buildService(ProductService::class.java).getSizeProduct(5)
            productApi?.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<Product>>>,
                    response: Response<ApiResponse<MutableList<Product>>>
                ) {
                    val res = response.body()!!
                    newArrayFoodList = res.data
                    binding.foodRecyclerView.adapter = activity?.let { FoodAdapter(it,newArrayFoodList) }
                    binding.progressBar2.visibility = ProgressBar.GONE
                }

                override fun onFailure(call: Call<ApiResponse<MutableList<Product>>>, t: Throwable) {}
            })
        }
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
                    if (listNotification.size > 0)
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