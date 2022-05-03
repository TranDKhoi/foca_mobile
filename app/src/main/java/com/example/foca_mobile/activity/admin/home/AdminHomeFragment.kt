package com.example.foca_mobile.activity.admin.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.order.AdminOrderManagement
import com.example.foca_mobile.databinding.FragmentAdminHomeBinding


class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recentOrderList: ArrayList<RecentOrderClass>
    private lateinit var myMenuList: ArrayList<MyMenuClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminHomeBinding.inflate(layoutInflater)

        createRecentOrderRCV()
        binding.recentOrderRCV.adapter = RecentOrderAdapter(recentOrderList)

        createMyMenuRCV()
        binding.myMenuRCV.adapter = MyMenuAdapter(myMenuList)

        binding.viewMoreOrder.setOnClickListener {
            val intent = Intent(context, AdminOrderManagement::class.java)
            startActivity(intent);
        }




        return binding.root
    }

    private fun createMyMenuRCV() {
        binding.myMenuRCV.layoutManager = LinearLayoutManager(activity)
        binding.myMenuRCV.setHasFixedSize(true)
        myMenuList = arrayListOf()

        for (i in 0..4) {
            myMenuList.add(
                MyMenuClass(
                    R.drawable.favorite_8,
                    "Lê Hải Phong",
                    20,
                    R.drawable.ic_edit
                )
            )
        }
    }

    private fun createRecentOrderRCV() {

        binding.recentOrderRCV.setHasFixedSize(true)

        recentOrderList = arrayListOf()
        recentOrderList.add(
            RecentOrderClass(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20
            )
        )
        recentOrderList.add(
            RecentOrderClass(
                R.drawable.favorite_9,
                "Trần Thị Nhu",
                20
            )
        )
        recentOrderList.add(
            RecentOrderClass(
                R.drawable.favorite_8,
                "Lê Hải Phong",
                20
            )
        )
        recentOrderList.add(
            RecentOrderClass(
                R.drawable.favorite_9,
                "Trần Thị Nhu",
                20
            )
        )

    }
}