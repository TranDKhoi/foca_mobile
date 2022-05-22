package com.example.foca_mobile.activity.user.cart_order.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.UserDetailOrder
import com.example.foca_mobile.databinding.ListOrderItemBinding
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
import java.text.DecimalFormat

class RecyclerViewAdapterOrder(private val listOrder: MutableList<Order>) :
    RecyclerView.Adapter<RecyclerViewAdapterOrder.OrderViewHolder>() {

    var onItemClick: ((MutableList<OrderDetails>, Order) -> Unit)? = null

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
        holder.binding.orderItemStatus.text = getStatus(item, holder.binding.root)
        holder.binding.orderQuantity.text =
            item.orderDetails?.size.toString() + " " + holder.binding.root.resources.getString(R.string.item)
        holder.binding.orderItemPrice.text =
            dec.format(item.orderDetails?.get(0)?.product?.price) + "đ"
        holder.binding.orderItemQuantity.text = "x" + item.orderDetails?.get(0)?.quantity.toString()
        holder.binding.orderTotalPrice.text = dec.format(item.totalPrice) + "đ"
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.binding.root.context, UserDetailOrder::class.java)
            intent.putExtra("orderid", item.id)
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    private fun getStatus(order: Order, root: CardView): String {
        when (order.status) {
            "ARRIVED" -> return root.resources.getString(R.string.ARRIVED)
            "PENDING" -> return root.resources.getString(R.string.PENDING)
            "PROCESSED" -> return root.resources.getString(R.string.PROCESSED)
            "COMPLETED" -> return root.resources.getString(R.string.COMPLETED)
            "CANCELLED" -> return root.resources.getString(R.string.CANCELLED)
        }
        return ""
    }
}

