package com.example.foca_mobile.activity.admin.order.allorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.foca_mobile.databinding.FragmentAdminOrderBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminOrderManagement : Fragment() {

    private var _binding: FragmentAdminOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderList: MutableList<Order>
    private lateinit var adapter: AllOrderAdapter
    private var currentFilter = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminOrderBinding.inflate(
            inflater,
            container,
            false
        )
        currentFilter = ""
        orderList = arrayListOf()
        adapter = AllOrderAdapter(orderList)
        binding.orderRCV.adapter = adapter

        getOrder()

        //FILTER BUTTON
        binding.filterBtn.setOnClickListener {
            // setup the alert builder
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle("Filter the orders")

            // add a list
            val status = arrayOf("ALL", "PENDING", "PROCESSED", "COMPLETED", "CANCELLED")
            builder.setItems(status) { _, which ->
                when (which) {
                    0 -> {
                        getOrder()
                        currentFilter = ""
                    }
                    1 -> {
                        getOrderByStatus("PENDING")
                        currentFilter = "PENDING"
                    }
                    2 -> {
                        getOrderByStatus("PROCESSED")
                        currentFilter = "PROCESSED"
                    }
                    3 -> {
                        getOrderByStatus("COMPLETED")
                        currentFilter = "COMPLETED"
                    }
                    4 -> {
                        getOrderByStatus("CANCELLED")
                        currentFilter = "CANCELLED"
                    }
                }
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (currentFilter == "")
            getOrder()
        else
            getOrderByStatus(currentFilter)
    }

    private fun getOrder() {
        //CALL API
        val allOrderCall = ServiceGenerator.buildService(OrderService::class.java)
            .getAllOrder()
        binding.bar.visibility = ProgressBar.VISIBLE
        allOrderCall?.enqueue(object : Callback<ApiResponse<MutableList<Order>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Order>>>,
                response: Response<ApiResponse<MutableList<Order>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Order>> = response.body()!!
                    orderList = res.data
                    adapter = AllOrderAdapter(orderList)
                    binding.orderRCV.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.bar.visibility = ProgressBar.GONE
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Order>>>, t: Throwable) {
            }
        })
    }

    private fun getOrderByStatus(status: String) {
        //CALL API
        val allOrderCall = ServiceGenerator.buildService(OrderService::class.java)
            .getOrderByStatus(status)
        binding.bar.visibility = ProgressBar.VISIBLE
        allOrderCall?.enqueue(object : Callback<ApiResponse<MutableList<Order>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Order>>>,
                response: Response<ApiResponse<MutableList<Order>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Order>> = response.body()!!
                    orderList = res.data
                    adapter = AllOrderAdapter(orderList)
                    binding.orderRCV.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.bar.visibility = ProgressBar.GONE
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Order>>>, t: Throwable) {
            }
        })
    }
}