package com.example.foca_mobile.activity.user.cart_order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.`object`.Food

class RecyclerViewAdapterOrderDetail(private val listFood: ArrayList<Food>) : RecyclerView.Adapter<RecyclerViewAdapterOrderDetail.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var img : ImageView = itemView.findViewById(R.id.foodItemImage)
        var name : TextView = itemView.findViewById(R.id.foodItemName)
        var title : TextView = itemView.findViewById(R.id.foodItemTitle)
        var price: TextView = itemView.findViewById(R.id.foodItemPrice)
        var quantity : TextView = itemView.findViewById(R.id.foodItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_order_food_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFood[position]
        holder.img.setImageResource(item.imgSrc)
        holder.name.text=item.name
        holder.title.text=item.option
        holder.price.text=item.price.toString()
        holder.quantity.text=item.quantity.toString()


    }

    override fun getItemCount(): Int {
        return listFood.size
    }

}