package com.example.foca_mobile.activity.user.home.infofood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.ReviewFood
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ReviewFoodAdapter(private val arrayList: ArrayList<ReviewFood>) :
    RecyclerView.Adapter<ReviewFoodAdapter.ViewHolder> (){

    private var formatter :DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item) {
        val imageAvatar : ImageView = item.findViewById(R.id.avatarPerson)
        val txtName : TextView = item.findViewById(R.id.namePerson)
        val date : TextView = item.findViewById(R.id.date)
        val txtEvaluate : TextView = item.findViewById(R.id.evaluate)
        val numberStar : TextView = item.findViewById(R.id.numberStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_review_food,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageAvatar.setImageResource(arrayList[position].imageAvatar)
        holder.txtName.text = arrayList[position].namePerson
        holder.date.text = arrayList[position].date.format(formatter)
        holder.txtEvaluate.text = arrayList[position].evaluate
        holder.numberStar.text = arrayList[position].numberStar.toString()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}