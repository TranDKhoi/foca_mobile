package com.example.foca_mobile.activity.admin.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.`object`.Order
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrder
import com.example.foca_mobile.databinding.ActivityAdminOrderManagementBinding

class AdminOrderManagement : AppCompatActivity() {

    private lateinit var binding: ActivityAdminOrderManagementBinding
    private val orderList = ArrayList<Order>()
    private lateinit var adapter: RecyclerViewAdapterOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createOrderRCV()

        binding.backBtn.setOnClickListener {
            this.finish()
        }


        binding

    }

    private fun createOrderRCV() {
        val listFood: ArrayList<Food> = ArrayList()
        listFood.add(Food("Spacy fresh crab", "Waroenk kita", 100000, 1, R.drawable.image_logo))
        listFood.add(Food("Sushi", "Waroenk kita", 25000, 1, R.drawable.image_logo))

        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))
        orderList.add(Order("Tôm hùm", 100000, "Progress", listFood))

        adapter = RecyclerViewAdapterOrder(orderList)
        binding.orderRCV.layoutManager = LinearLayoutManager(applicationContext)
        binding.orderRCV.adapter = adapter
    }
}