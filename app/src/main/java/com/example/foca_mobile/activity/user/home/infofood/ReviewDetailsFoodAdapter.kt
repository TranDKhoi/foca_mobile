package com.example.foca_mobile.activity.user.home.infofood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ListReviewDetailsFoodItemBinding
import com.example.foca_mobile.model.Review
import java.text.SimpleDateFormat

class ReviewDetailsFoodAdapter(private val arrayList : MutableList<Review>) :
        RecyclerView.Adapter<ReviewDetailsFoodAdapter.ViewHolder> () {

    inner class ViewHolder (val binding : ListReviewDetailsFoodItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ReviewDetailsFoodAdapter.ViewHolder {
        val v = ListReviewDetailsFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReviewDetailsFoodAdapter.ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(arrayList[position].user?.photoUrl)
            .into(holder.binding.imagePerson)
        holder.binding.namePerson.text = arrayList[position].user?.fullName
        holder.binding.description.text = arrayList[position].content
        holder.binding.rBar.rating = arrayList[position].rating!!.toFloat()
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputDate = inputDateFormat.parse(arrayList[position].createdAt)
        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy")
        holder.binding.createAtTxt.text = outputDateFormat.format(outputDate)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}
