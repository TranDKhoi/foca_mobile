package com.example.foca_mobile.activity.user.cart_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ListRatingFoodItemBinding
import com.example.foca_mobile.model.Review


class RecyclerViewAdapterRatingFood(private val listOrderDetails: MutableList<Review>) :
    RecyclerView.Adapter<RecyclerViewAdapterRatingFood.RatingFoodViewHolder>() {

    inner class RatingFoodViewHolder(val binding: ListRatingFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingFoodViewHolder {
        val view =
            ListRatingFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingFoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatingFoodViewHolder, position: Int) {
        val item = listOrderDetails[position]
        Glide.with(holder.itemView.context)
            .load(item.orderDetail?.product?.image)
            .into(holder.binding.ratingFoodImage)
        holder.binding.ratingFoodName.text = item.orderDetail?.product!!.name
        holder.binding.ratingFoodTitle.text = item.orderDetail?.product!!.description
        holder.binding.ratingFoodPrice.text = item.orderDetail!!.product?.price.toString()
        holder.binding.ratingFoodQuantity.text = "x ${item.orderDetail!!.quantity}"
        holder.binding.ratingFoodBar.stepSize = 1f
        holder.binding.ratingFoodBar.rating= 5F
        holder.binding.ratingFoodBar.setOnRatingBarChangeListener { _, rating, _ ->
            item.rating = rating.toInt()
        }
        holder.binding.ratingFoodReview.addTextChangedListener {
            item.content = holder.binding.ratingFoodReview.text.toString()
        }
    }


    override fun getItemCount(): Int {
        return listOrderDetails.size
    }
}
