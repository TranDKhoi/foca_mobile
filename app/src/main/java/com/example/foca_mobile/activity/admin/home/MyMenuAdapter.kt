package com.example.foca_mobile.activity.admin.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListFoodItemBinding

class MyMenuAdapter(private val arrayList: ArrayList<MyMenuClass>) :
    RecyclerView.Adapter<MyMenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(val binding: ListFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onItemClick: ((MyMenuClass) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val v = ListFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(v)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.binding.imageFood.setImageResource(arrayList[position].imageFood)
        holder.binding.nameFood.text = arrayList[position].nameFood
        holder.binding.price.text = arrayList[position].price.toString() + "$"
        holder.binding.iconButton.setBackgroundResource(arrayList[position].iconButton)
        holder.binding.iconButton.scaleX = 0.5f
        holder.binding.iconButton.scaleY = 0.5f
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(arrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}