package com.example.foca_mobile.activity.user.cart_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.adapter.CartOrderAdapter
import com.example.foca_mobile.databinding.FragmentUserMyCartBinding
import com.example.foca_mobile.utils.GlobalObject
import com.google.android.material.tabs.TabLayoutMediator

class UserMyCart : Fragment() {

    private lateinit var tabTitle: ArrayList<String>
    private lateinit var binding: FragmentUserMyCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_my_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabTitle = arrayListOf(
            activity?.resources!!.getString(R.string.Mycart),
            activity?.resources!!.getString(R.string.MyOrders)
        )

        val fragmentManager = childFragmentManager
        binding = FragmentUserMyCartBinding.bind(view)
        binding.myCartViewPager.adapter = CartOrderAdapter(fragmentManager, lifecycle)
        binding.myCartViewPager.isUserInputEnabled = false
        binding.myCartViewPager.isSaveEnabled = false

        TabLayoutMediator(binding.myCartTabLayout, binding.myCartViewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        if (GlobalObject.isUserOpenRecentOrder) {
            binding.myCartViewPager.setCurrentItem(1, true)
        } else
            binding.myCartViewPager.setCurrentItem(0, true)

    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalObject.isUserOpenRecentOrder = false
    }

}