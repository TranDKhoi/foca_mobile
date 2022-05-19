package com.example.foca_mobile.activity.user.home.infofood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ListReviewFoodItemBinding
import com.example.foca_mobile.model.Review
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ReviewFoodAdapter(private val arrayList: MutableList<Review>) :
    RecyclerView.Adapter<ReviewFoodAdapter.ViewHolder> (){

    inner class ViewHolder(val binding: ListReviewFoodItemBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ListReviewFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(arrayList[position].user?.photoUrl)
            .into(holder.binding.avatarPerson)
        holder.binding.namePerson.text = arrayList[position].user?.fullName
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputDate = inputDateFormat.parse(arrayList[position].createdAt)
        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy")
        holder.binding.date.text = outputDateFormat.format(outputDate)
        holder.binding.evaluate.text = arrayList[position].content
        holder.binding.numberStar.text = arrayList[position].rating.toString()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}