package com.example.foca_mobile.activity.user.cart_order.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foca_mobile.activity.user.cart_order.fragment.MyCartFragment
import com.example.foca_mobile.activity.user.cart_order.fragment.MyOrderFragment

class CartOrderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> MyCartFragment()
            1-> MyOrderFragment()
            else -> MyCartFragment()
        }
    }

}