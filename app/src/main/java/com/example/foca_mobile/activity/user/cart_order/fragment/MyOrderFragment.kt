package com.example.foca_mobile.activity.user.cart_order.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrder
import com.example.foca_mobile.databinding.FragmentMyOrdersBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderFragment : Fragment() {
    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!
    private var listOrder: MutableList<Order>? = null
    private var adapter: RecyclerViewAdapterOrder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        getListOrder(this.context)


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getListOrder(this.context)
    }

    private fun sortOrder() {
        val listArrived : ArrayList<Order> = ArrayList()
        val listPending : ArrayList<Order> = ArrayList()
        val listProcessed : ArrayList<Order> = ArrayList()
        val listCompleted : ArrayList<Order> = ArrayList()
        for (item in listOrder!!){
            when (item.status) {
                "ARRIVED" -> listArrived.add(item)
                "PENDING" -> listPending.add(item)
                "PROCESSED" -> listProcessed.add(item)
                "COMPLETED" -> {
                    if (item.isReviewed){
                        listCompleted.add(item)
                    }
                    else{
                        listCompleted.add(0,item)
                    }
                }
            }
        }
        val tempList : ArrayList<Order> = ArrayList()
        tempList.addAll(listArrived)
        tempList.addAll(listPending)
        tempList.addAll(listProcessed)
        tempList.addAll(listCompleted)
        listOrder!!.clear()
        listOrder = tempList.toMutableList()

    }

    private fun getListOrder(context: Context?) {
        val listOrderCall = ServiceGenerator.buildService(OrderService::class.java).getUserOrder()
        binding.bar.visibility = ProgressBar.VISIBLE
        listOrderCall.enqueue(object : Callback<ApiResponse<MutableList<Order>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Order>>>,
                response: Response<ApiResponse<MutableList<Order>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Order>> = response.body()!!
                    listOrder = res.data
                    sortOrder()
                    adapter = RecyclerViewAdapterOrder(listOrder!!)
                    binding.rvOrder.adapter = adapter
                    binding.rvOrder.layoutManager = LinearLayoutManager(activity)
                    binding.bar.visibility = ProgressBar.GONE

                } else {
                    Toast.makeText(context, "Call api else error", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Order>>>, t: Throwable) {
                Toast.makeText(context, "Call api failure error", Toast.LENGTH_LONG).show()
            }
        })
    }
}
