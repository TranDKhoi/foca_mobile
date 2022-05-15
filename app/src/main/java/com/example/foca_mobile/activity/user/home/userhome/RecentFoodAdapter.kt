package com.example.foca_mobile.activity.user.home.userhome

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.activity.user.home.infofood.InfoFood_Activity
import com.example.foca_mobile.databinding.ListRecentFoodItemBinding
import com.example.foca_mobile.model.Product
import java.text.DecimalFormat

class RecentFoodAdapter(private var c: Context, private val arrList: MutableList<Product?>) :
    RecyclerView.Adapter<RecentFoodAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListRecentFoodItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentFoodAdapter.ViewHolder {
        val v = ListRecentFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.binding.root.context)
            .load(arrList[position]?.image)
            .into(holder.binding.imageRecentFood)
        holder.binding.nameRecentFood.text  = arrList[position]?.name
        holder.binding.createAtTxt.text = arrList[position]?.description
        val dec = DecimalFormat("#,###")
        holder.binding.priceRecentFood.text = dec.format(arrList[position]?.price) + "đ"
        holder.binding.root.setOnClickListener {
            var intent = Intent(c, InfoFood_Activity::class.java)
            intent.putExtra("Product",arrList[position])
            c.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }
}