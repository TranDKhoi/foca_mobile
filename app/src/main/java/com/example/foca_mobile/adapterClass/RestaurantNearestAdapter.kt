package com.example.foca_mobile.adapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.RestaurantNearest

class RestaurantNearestAdapter(private val arrList: ArrayList<RestaurantNearest>) : RecyclerView.Adapter<RestaurantNearestAdapter.ViewHolder>() {
    class ViewHolder (item : View) : RecyclerView.ViewHolder(item){
        val image : ImageView = item.findViewById(R.id.imageNearest)
        val txtName : TextView = item.findViewById(R.id.nameNearest)
        val txtNumber: TextView = item.findViewById(R.id.numberNearest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_nearest_restaurant,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(arrList[position].image)
        holder.txtName.text = arrList[position].txtName
        holder.txtNumber.text = arrList[position].txtNumber
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}