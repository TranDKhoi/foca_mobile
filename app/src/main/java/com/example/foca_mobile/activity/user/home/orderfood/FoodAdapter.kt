package com.example.foca_mobile.activity.user.home.orderfood

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.activity.user.home.infofood.InfoFood_Activity
import com.example.foca_mobile.databinding.ListFoodItemBinding


class FoodAdapter(private var c: Context, private val arrayList: ArrayList<Food>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder> (){

    inner class FoodViewHolder(val binding: ListFoodItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val v = ListFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(v)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.binding.imageFood.setImageResource(arrayList[position].imageFood)
        holder.binding.nameFood.text = arrayList[position].nameFood
        holder.binding.price.text = arrayList[position].price.toString() + "$"
        holder.binding.iconButton.setBackgroundResource(arrayList[position].iconButton)
        holder.binding.root.setOnClickListener {
            var intent = Intent(c,InfoFood_Activity::class.java)
            c.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}