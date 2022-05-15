package com.example.foca_mobile.activity.admin.menu

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.menu.productdetail.AdminProductDetail
import com.example.foca_mobile.databinding.ListFoodItemBinding
import com.example.foca_mobile.model.Product
import com.google.gson.Gson
import java.text.NumberFormat

class MyMenuAdapter(private val productList: MutableList<Product>) :
    RecyclerView.Adapter<MyMenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(val binding: ListFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val v = ListFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(v)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

        Glide.with(holder.itemView.context)
            .load(productList[position].image)
            .into(holder.binding.imageFood)
        holder.binding.nameFood.text = productList[position].name
        holder.binding.price.text =
            NumberFormat.getCurrencyInstance().format(productList[position].price)
        holder.binding.description.text = productList[position].description
        holder.binding.iconButton.setBackgroundResource(R.drawable.ic_edit)
        holder.binding.iconButton.scaleX = 0.5f
        holder.binding.iconButton.scaleY = 0.5f
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminProductDetail::class.java)
            intent.putExtra("product", Gson().toJson(productList[position]))
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.cardView3.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminCreateProduct::class.java)
            intent.putExtra("product", Gson().toJson(productList[position]))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}