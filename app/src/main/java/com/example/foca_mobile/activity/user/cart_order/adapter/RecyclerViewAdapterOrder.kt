package com.example.foca_mobile.activity.user.cart_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ListOrderItemBinding
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
import java.text.DecimalFormat

class RecyclerViewAdapterOrder(private val listOrder: MutableList<Order>) : RecyclerView.Adapter<RecyclerViewAdapterOrder.OrderViewHolder>() {

    var onItemClick : ((MutableList<OrderDetails>, Order)-> Unit)? = null
    inner class OrderViewHolder(val binding: ListOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = ListOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = listOrder[position]
        val dec = DecimalFormat("#,###")
        Glide.with(holder.itemView.context)
            .load(item.orderDetails?.get(0)?.product?.image)
            .into(holder.binding.orderItemImage)
        holder.binding.orderItemName.text = item.orderDetails?.get(0)?.product?.name
        holder.binding.orderItemStatus.text = item.status
        holder.binding.orderQuantity.text = item.orderDetails?.size.toString() + " sản phẩm"
        holder.binding.orderItemPrice.text = dec.format(item.orderDetails?.get(0)?.product?.price) + "đ"
        holder.binding.orderItemQuantity.text = "x"+ item.orderDetails?.get(0)?.quantity.toString()
        holder.binding.orderTotalPrice.text = dec.format(item.totalPrice) + "đ"

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item.orderDetails!!, item)
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}

