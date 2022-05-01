package com.example.foca_mobile.activity.user.home.infofood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListReviewFoodBinding
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ReviewFoodAdapter(private val arrayList: ArrayList<ReviewFood>) :
    RecyclerView.Adapter<ReviewFoodAdapter.ViewHolder> (){

    private var formatter :DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    inner class ViewHolder(val binding: ListReviewFoodBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ListReviewFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.avatarPerson.setImageResource(arrayList[position].imageAvatar)
        holder.binding.namePerson.text = arrayList[position].namePerson
        holder.binding.date.text = arrayList[position].date.format(formatter)
        holder.binding.evaluate.text = arrayList[position].evaluate
        holder.binding.numberStar.text = arrayList[position].numberStar.toString()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}