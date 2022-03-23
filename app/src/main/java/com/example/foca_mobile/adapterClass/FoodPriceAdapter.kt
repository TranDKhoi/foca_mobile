package com.example.foca_mobile.adapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.FoodPrice

class FoodPriceAdapter (private var arrFood : ArrayList<FoodPrice>)
    : RecyclerView.Adapter<FoodPriceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_food_price,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodPriceAdapter.ViewHolder, position: Int){
        holder.imageFood.setImageResource(arrFood[position].imageFood)
        holder.txtNameFood.text = arrFood[position].nameFood
        holder.typeFood.text = arrFood[position].typeFood
        holder.txtPrice.text = arrFood[position].price
    }

    override fun getItemCount(): Int {
        return arrFood.size
    }
    class ViewHolder (item : View) : RecyclerView.ViewHolder(item){
        val imageFood : ImageView = item.findViewById(R.id.imageFood)
        val txtNameFood : TextView = item.findViewById(R.id.nameFood)
        val typeFood : TextView = item.findViewById(R.id.typeFood)
        val txtPrice : TextView = item.findViewById(R.id.price)
    }
}