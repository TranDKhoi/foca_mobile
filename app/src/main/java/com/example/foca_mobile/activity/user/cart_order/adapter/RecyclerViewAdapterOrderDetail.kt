package com.example.foca_mobile.activity.user.cart_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ListOrderFoodItemBinding
import com.example.foca_mobile.model.OrderDetails
import java.text.DecimalFormat

class RecyclerViewAdapterOrderDetail(private val listOrderDetails: MutableList<OrderDetails>) : RecyclerView.Adapter<RecyclerViewAdapterOrderDetail.OrderDetailViewHolder>() {

    inner class OrderDetailViewHolder(val binding: ListOrderFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val view = ListOrderFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val item = listOrderDetails[position]
        val dec = DecimalFormat("#,###")
        Glide.with(holder.itemView.context)
            .load(item.product?.image)
            .into(holder.binding.foodItemImage)
        holder.binding.foodItemName.text= item.product!!.name
        holder.binding.foodItemTitle.text= item.product!!.description
        holder.binding.foodItemPrice.text= dec.format(item.price) + "đ"
        holder.binding.foodItemQuantity.text="x ${item.quantity}"
    }

    override fun getItemCount(): Int {
        return listOrderDetails.size
    }
}