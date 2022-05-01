package com.example.foca_mobile.activity.user.cart_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.adapter.CartOrderAdapter
import com.example.foca_mobile.databinding.FragmentMyCartBinding
import com.example.foca_mobile.databinding.FragmentUserMyCartBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserMyCart : Fragment() {

    private var tabTitle = arrayOf("My cart","My orders")
    private  lateinit var  binding: FragmentUserMyCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_my_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        binding = FragmentUserMyCartBinding.bind(view)
        binding.myCartViewPager.adapter = CartOrderAdapter(fragmentManager, lifecycle)
        binding.myCartViewPager.isUserInputEnabled=false

        TabLayoutMediator(binding.myCartTabLayout, binding.myCartViewPager){
                tab, position ->
            tab.text = tabTitle[position]

        }.attach()
    }

}