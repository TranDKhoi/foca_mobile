package com.example.foca_mobile.activity.user.home.orderfood

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.home.infofood.InfoFood_Activity
import com.example.foca_mobile.databinding.ListFoodItemBinding
import com.example.foca_mobile.model.Product
import java.text.DecimalFormat


class FoodAdapter(private var c: Context, private val arrayList: MutableList<Product>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(val binding: ListFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val v = ListFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(v)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(arrayList[position].image)
            .into(holder.binding.imageFood)
        holder.binding.nameFood.text = arrayList[position].name
        val dec = DecimalFormat("#,###")
        holder.binding.price.text = dec.format(arrayList[position].price!!.toInt()).plus("đ")
        holder.binding.iconButton.setBackgroundResource(R.drawable.ic_add)
        holder.binding.numberProduct.text =
            holder.binding.root.resources.getString(R.string.Sold).plus(" ")
                .plus(arrayList[position].orderCount.toString())
        holder.binding.root.setOnClickListener {
            val intent = Intent(c, InfoFood_Activity::class.java)
            intent.putExtra("id", arrayList[position].id)
            c.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}