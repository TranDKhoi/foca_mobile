package com.example.foca_mobile.activity.user.cart_order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.`object`.Order

class RecyclerViewAdapterOrder(private val listOrder: ArrayList<Order>) : RecyclerView.Adapter<RecyclerViewAdapterOrder.ViewHolder>() {

    var onItemClick : ((ArrayList<Food>)-> Unit)? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var img : ImageView = itemView.findViewById(R.id.orderItemImage)
        var name : TextView = itemView.findViewById(R.id.orderItemName)
        var price: TextView = itemView.findViewById(R.id.orderItemPrice)
        var status : Button = itemView.findViewById(R.id.orderItemStatus)
        var quantity : TextView = itemView.findViewById(R.id.orderQuantity)
        var totalPrice : TextView = itemView.findViewById(R.id.orderTotalPrice)
        var itemQuantity : TextView = itemView.findViewById(R.id.orderItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_order_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOrder[position]
        var total: Int = 0
        holder.img.setImageResource(item.listFood[0].imgSrc)
        holder.name.text = item.name
        holder.status.text = item.status
        holder.quantity.text = item.listFood.size.toString() + " sản phẩm"
        holder.price.text = item.listFood[0].price.toString()
        holder.itemQuantity.text = "x"+item.listFood[0].quantity.toString()

        val listFood: ArrayList<Food> = item.listFood
        for(everyItem in listFood){
            total += everyItem.price
        }
        holder.totalPrice.text = total.toString()

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item.listFood)
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}