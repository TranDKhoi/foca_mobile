package com.example.foca_mobile.activity.admin.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.menu.AdminMenu
import com.example.foca_mobile.activity.admin.menu.MyMenuAdapter
import com.example.foca_mobile.activity.admin.order.allorder.AdminOrderManagement
import com.example.foca_mobile.activity.user.notifi.UserNotification
import com.example.foca_mobile.databinding.FragmentAdminHomeBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.example.foca_mobile.utils.GlobalObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    private var arrivedOrderList: MutableList<Order>? = null
    private lateinit var myMenuList: MutableList<Product>
    private lateinit var arrivedAdapter: ArrivedOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminHomeBinding.inflate(layoutInflater)

        arrivedOrderList = mutableListOf()
        arrivedAdapter = ArrivedOrderAdapter(arrivedOrderList!!)
        getArrivedOrder()

        getMyMenu()

        binding.viewMoreOrder.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.wrapper, AdminOrderManagement())
                commit()
                GlobalObject.bottomNavigation.setItemSelected(R.id.order)
            }
        }

        binding.viewMoreMenu.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.wrapper, AdminMenu())
                commit()
                GlobalObject.bottomNavigation.setItemSelected(R.id.menu)
            }
        }

        binding.notifyBtn.setOnClickListener {
            val intent = Intent(context, UserNotification::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    private fun getMyMenu() {
        //CALL API
        val myMenuCall = ServiceGenerator.buildService(ProductService::class.java)
            .getProductList("", 1000)

        myMenuCall?.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Product>>>,
                response: Response<ApiResponse<MutableList<Product>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Product>> = response.body()!!
                    myMenuList = res.data
                    binding.myMenuRCV.adapter =
                        MyMenuAdapter(myMenuList)
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<Product>>>,
                t: Throwable
            ) {
            }
        })
    }

    private fun getArrivedOrder() {
        //CALL API
        val pendingOrderCall = ServiceGenerator.buildService(OrderService::class.java)
            .getOrderByStatus("ARRIVED")

        pendingOrderCall?.enqueue(object : Callback<ApiResponse<MutableList<Order>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Order>>>,
                response: Response<ApiResponse<MutableList<Order>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Order>> = response.body()!!
                    arrivedOrderList = res.data
                    binding.arrivedOrderRCV.adapter =
                        ArrivedOrderAdapter(arrivedOrderList!!)
                    if (arrivedOrderList!!.count() == 0)
                        binding.logoDone.visibility = ImageView.VISIBLE
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Order>>>, t: Throwable) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getMyMenu()
        getArrivedOrder()
    }

}