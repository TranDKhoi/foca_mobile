package com.example.foca_mobile.activity.admin.order.allorder

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.order.orderdetail.AdminOrderDetail
import com.example.foca_mobile.databinding.ListOrderItemBinding
import com.example.foca_mobile.model.Order
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AllOrderAdapter(private val listOrder: MutableList<Order>) :
    RecyclerView.Adapter<AllOrderAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ListOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOrder[position]

        Glide.with(holder.itemView.context)
            .load(item.buyer?.photoUrl)
            .into(holder.binding.orderItemImage)
        holder.binding.orderItemName.text = item.buyer?.fullName
        holder.binding.orderItemQuantity.text = item.notes

        val parsedDate = LocalDateTime.parse(item.createdAt, DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
        holder.binding.orderItemPrice.text = formattedDate

        when (item.status) {
            "ARRIVED" -> {
                holder.binding.orderItemStatus.text =
                    holder.binding.root.resources.getString(R.string.ARRIVED)
                holder.binding.orderItemStatus.setBackgroundResource(R.drawable.light_red_gradient)
            }
            "PENDING" -> {
                holder.binding.orderItemStatus.text =
                    holder.binding.root.resources.getString(R.string.PENDING)
                holder.binding.orderItemStatus.setBackgroundResource(R.drawable.light_yellow_gradient)
            }
            "PROCESSED" -> {
                holder.binding.orderItemStatus.text =
                    holder.binding.root.resources.getString(R.string.PROCESSED)
                holder.binding.orderItemStatus.setBackgroundResource(R.drawable.light_green_gradient)
            }
            "COMPLETED" -> {
                holder.binding.orderItemStatus.text =
                    holder.binding.root.resources.getString(R.string.COMPLETED)
                holder.binding.orderItemStatus.setBackgroundResource(R.drawable.light_green_gradient)
            }
            "CANCELLED" -> {
                holder.binding.orderItemStatus.text =
                    holder.binding.root.resources.getString(R.string.CANCELLED)
                holder.binding.orderItemStatus.setBackgroundResource(R.drawable.light_gray_gradient)
            }
        }
        holder.binding.orderQuantity.text =
            "${item.orderDetails?.size} ${holder.binding.root.context.resources.getString(R.string.item)}"

        val dec = DecimalFormat("#,###")
        holder.binding.orderTotalPrice.text = dec.format(item.totalPrice).plus("??")

        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminOrderDetail::class.java)
            intent.putExtra("orderid", item.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}