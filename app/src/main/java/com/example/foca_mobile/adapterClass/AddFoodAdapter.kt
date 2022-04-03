package com.example.foca_mobile.adapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.AddFood

class AddFoodAdapter(private val arrayList: ArrayList<AddFood>) :
    RecyclerView.Adapter<AddFoodAdapter.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFoodAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_add_food,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddFoodAdapter.ViewHolder, position: Int) {
        holder.imageFood.setImageResource(arrayList[position].imageFood)
        holder.txtName.text = arrayList[position].nameFood
        holder.price.text = arrayList[position].price.toString() + "$"
        holder.iconButton.setImageResource(arrayList[position].iconButton)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item) {
        val imageFood : ImageView = item.findViewById(R.id.imageFood)
        val txtName : TextView = item.findViewById(R.id.nameFood)
        val price : TextView = item.findViewById<TextView?>(R.id.price)
        val iconButton : ImageButton = item.findViewById(R.id.iconButton)
    }
}