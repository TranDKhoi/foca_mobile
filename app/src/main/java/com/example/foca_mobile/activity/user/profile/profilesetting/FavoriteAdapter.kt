package com.example.foca_mobile.activity.user.profile.profilesetting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.databinding.ListFavoriteBinding
import com.example.foca_mobile.model.Favorite

class FavoriteAdapter(private val arrayList:List<Favorite>):
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>()
{
    inner class FavoriteViewHolder(val binding:ListFavoriteBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val v = ListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.imageFavorite.setImageResource(arrayList[position].imageFavorite)
        holder.binding.nameFavorite.text = arrayList[position].nameFavorite
        holder.binding.nameResFavorite.text = arrayList[position].nameResFavorite
        holder.binding.price.text = "$" + arrayList[position].price.toString()
        holder.binding.root.setOnClickListener {}
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}