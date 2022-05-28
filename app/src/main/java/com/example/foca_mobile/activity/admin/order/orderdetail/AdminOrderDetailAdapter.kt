package com.example.foca_mobile.activity.admin.order.orderdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ListAdminOrderDetailBinding
import com.example.foca_mobile.model.OrderDetails
import java.text.DecimalFormat
import java.text.NumberFormat

class AdminOrderDetailAdapter(
    private val listDetailOrder: MutableList<OrderDetails>
) :
    RecyclerView.Adapter<AdminOrderDetailAdapter.ViewHolder>() {


    class ViewHolder(val binding: ListAdminOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ListAdminOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDetailOrder[position]

        Glide.with(holder.itemView.context)
            .load(item.product?.image)
            .into(holder.binding.foodItemImage)
        holder.binding.foodItemName.text = item.product?.name
        holder.binding.foodItemTitle.text = item.product?.description

        val dec = DecimalFormat("#,###")
        holder.binding.foodItemPrice.text = dec.format(item.product?.price).plus("đ")
        holder.binding.foodItemQuantity.text = holder.binding.root.resources.getString(R.string.Qty).plus(item.quantity)
    }

    override fun getItemCount(): Int {
        return listDetailOrder.size
    }
}