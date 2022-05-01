package com.example.foca_mobile.activity.admin.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.activity.user.chat.listmess.ListMessageClass
import com.example.foca_mobile.activity.user.home.infofood.InfoFood_Activity
import com.example.foca_mobile.databinding.ListFoodBinding
import com.example.foca_mobile.model.Food

class MyMenuAdapter(private val arrayList: ArrayList<MyMenuClass>) :
    RecyclerView.Adapter<MyMenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(val binding: ListFoodBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick: ((MyMenuClass) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val v = ListFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(v)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.binding.imageFood.setImageResource(arrayList[position].imageFood)
        holder.binding.nameFood.text = arrayList[position].nameFood
        holder.binding.price.text = arrayList[position].price.toString() + "$"
        holder.binding.iconButton.setImageResource(arrayList[position].iconButton)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(arrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}