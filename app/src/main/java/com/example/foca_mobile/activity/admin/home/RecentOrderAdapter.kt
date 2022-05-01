package com.example.foca_mobile.activity.admin.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListRecentFoodBinding

class RecentOrderAdapter(private val orderList: ArrayList<RecentOrderClass>) :
    RecyclerView.Adapter<RecentOrderAdapter.MyViewHolder>() {


    class MyViewHolder(val binding: ListRecentFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            ListRecentFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imageRecentFood.setImageResource(orderList[position].image)
        holder.binding.nameRecentFood.text  = orderList[position].txtName
        holder.binding.priceRecentFood.text = orderList[position].price.toString() + "$"
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}