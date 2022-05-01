package com.example.foca_mobile.activity.user.home.userhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListRecentFoodItemBinding

class RecentFoodAdapter(private val arrList: ArrayList<RecentFood>) :
    RecyclerView.Adapter<RecentFoodAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListRecentFoodItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentFoodAdapter.ViewHolder {
        val v = ListRecentFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageRecentFood.setImageResource(arrList[position].image)
        holder.binding.nameRecentFood.text  = arrList[position].txtName
        holder.binding.priceRecentFood.text = arrList[position].price.toString() + "$"
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}