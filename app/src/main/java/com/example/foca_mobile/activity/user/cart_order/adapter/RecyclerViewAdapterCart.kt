package com.example.foca_mobile.activity.user.cart_order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.adapter.item.CartItemViewHolder

class RecyclerViewAdapterCart(private val  listCart: ArrayList<Food>) :
    RecyclerView.Adapter<CartItemViewHolder>() {
//    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var name: TextView = itemView.findViewById(R.id.cartItemName)
//        var option: TextView = itemView.findViewById(R.id.cartItemOption)
//        var price: TextView = itemView.findViewById(R.id.cartItemPrice)
//        var quantity: TextView = itemView.findViewById(R.id.cartItemQuantity)
//        var img: ImageView = itemView.findViewById(R.id.cartItemImage)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart_item, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = listCart[position]
        holder.name.text = item.name
        holder.option.text = item.option
        holder.price.text = item.price.toString()
        holder.quantity.text = item.quantity.toString()
        holder.img.setImageResource(item.imgSrc)
        holder as CartItemViewHolder
        holder.onDeleteClick = {
            removeItem(it as CartItemViewHolder)
        }
        holder.updateView()
    }

    override fun getItemCount(): Int {
        return  listCart.size
    }

    private fun removeItem(viewHolder: CartItemViewHolder){
        listCart.removeAt(viewHolder.bindingAdapterPosition)
        notifyItemRemoved(viewHolder.bindingAdapterPosition)
    }


}