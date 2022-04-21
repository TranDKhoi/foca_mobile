package com.example.foca_mobile.activity.user.cart_order.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.UserDetailOrder
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.`object`.Order
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrder
import com.example.foca_mobile.databinding.FragmentMyOrdersBinding

class MyOrderFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    private val list = ArrayList<Order>()
    private val adapter : RecyclerViewAdapterOrder = RecyclerViewAdapterOrder(list)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyOrdersBinding.bind(view)
        val adapter = RecyclerViewAdapterOrder(list)
        binding.rvOrder.layoutManager = LinearLayoutManager(activity)
        binding.rvOrder.adapter = adapter

        adapter.onItemClick={
            val intent = Intent(this.context, UserDetailOrder::class.java)
            intent.putExtra("listFood", it)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listFood: ArrayList<Food> = ArrayList()
        listFood.add(Food("Spacy fresh crab","Waroenk kita",100000,1,R.drawable.image_logo))
        listFood.add(Food("Sushi","Waroenk kita",25000,1,R.drawable.image_logo))
        listFood.add(Food("Beef steak","Waroenk kita",12000,1,R.drawable.image_logo))
        listFood.add(Food("Udon noodles","Waroenk kita",15000,1,R.drawable.image_logo))
        listFood.add(Food("Mỳ tôm hảo hảo","Waroenk kita",20000,1,R.drawable.image_logo))
        listFood.add(Food("Bánh hỏi heo quay","Waroenk kita",30000,1,R.drawable.image_logo))

        list.add(Order(R.drawable.image_logo, name = "Spacy fresh crab", totalPrice = 100000, status = "Process", listFood))
        list.add(Order(R.drawable.image_logo, name = "Order 1", totalPrice = 25000, status = "Process",listFood))
        list.add(Order(R.drawable.image_logo, name = "Order 2", totalPrice = 12000, status = "Process",listFood))
        list.add(Order(R.drawable.image_logo, name = "Order 3", totalPrice = 15000, status = "Process",listFood))
        list.add(Order(R.drawable.image_logo, name = "Order 4", totalPrice = 20000, status = "Process",listFood))
        list.add(Order(R.drawable.image_logo, name = "Order 5", totalPrice = 30000, status = "Process",listFood))

        adapter.notifyDataSetChanged()



    }



}