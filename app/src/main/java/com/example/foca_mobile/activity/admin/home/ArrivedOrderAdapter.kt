package com.example.foca_mobile.activity.admin.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.activity.admin.order.orderdetail.AdminOrderDetail
import com.example.foca_mobile.databinding.ListRecentFoodItemBinding
import com.example.foca_mobile.model.Order
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArrivedOrderAdapter(private val orderList: MutableList<Order>) :
    RecyclerView.Adapter<ArrivedOrderAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListRecentFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            ListRecentFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(holder.itemView.context)
            .load(orderList[position].orderDetails?.get(0)?.product?.image)
            .into(holder.binding.imageRecentFood)
        holder.binding.nameRecentFood.text = orderList[position].buyer?.fullName
        val dec = DecimalFormat("#,###")
        holder.binding.priceRecentFood.text = dec.format(orderList[position].totalPrice).plus("đ")

        val parsedDate =
            LocalDateTime.parse(orderList[position].createdAt, DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
        holder.binding.createAtTxt.text = formattedDate
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminOrderDetail::class.java)
            intent.putExtra("orderid", orderList[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}