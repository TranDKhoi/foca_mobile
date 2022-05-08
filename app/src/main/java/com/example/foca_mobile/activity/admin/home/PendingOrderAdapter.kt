package com.example.foca_mobile.activity.admin.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.activity.admin.order.orderdetail.AdminOrderDetail
import com.example.foca_mobile.databinding.ListRecentFoodItemBinding
import com.example.foca_mobile.model.Order
import com.google.gson.Gson
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PendingOrderAdapter(private val orderList: MutableList<Order>) :
    RecyclerView.Adapter<PendingOrderAdapter.MyViewHolder>() {

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

        val numberCurrency = NumberFormat.getCurrencyInstance()
        holder.binding.priceRecentFood.text = numberCurrency.format(orderList[position].totalPrice)

        val parsedDate =
            LocalDateTime.parse(orderList[position].createdAt, DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
        holder.binding.createAtTxt.text = formattedDate
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminOrderDetail::class.java)
            intent.putExtra("order", Gson().toJson(orderList[position]))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}