package com.example.foca_mobile.activity.user.home.userhome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListNearestRestaurantBinding
import com.example.foca_mobile.model.RestaurantNearest

class RestaurantNearestAdapter(private val arrList: ArrayList<RestaurantNearest>) :
    RecyclerView.Adapter<RestaurantNearestAdapter.RestaurantNearestViewHolder>() {

    inner class RestaurantNearestViewHolder(val binding: ListNearestRestaurantBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantNearestAdapter.RestaurantNearestViewHolder {
        val v = ListNearestRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantNearestViewHolder(v)
    }

    override fun onBindViewHolder(holder: RestaurantNearestViewHolder, position: Int) {
        holder.binding.imageNearest.setImageResource(arrList[position].image)
        holder.binding.nameNearest.text  = arrList[position].txtName
        holder.binding.numberNearest.text = arrList[position].txtNumber
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}