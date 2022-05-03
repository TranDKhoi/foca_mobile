package com.example.foca_mobile.activity.admin.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListRecentFoodItemBinding

class RecentOrderAdapter(private val orderList: ArrayList<RecentOrderClass>) :
    RecyclerView.Adapter<RecentOrderAdapter.MyViewHolder>() {

    var onItemClick: ((RecentOrderClass) -> Unit)? = null

    class MyViewHolder(val binding: ListRecentFoodItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            ListRecentFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imageRecentFood.setImageResource(orderList[position].image)
        holder.binding.nameRecentFood.text = orderList[position].txtName
        holder.binding.priceRecentFood.text = orderList[position].price.toString() + "$"
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(orderList[position])
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}