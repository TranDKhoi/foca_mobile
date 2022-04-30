package com.example.foca_mobile.adapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.Food


class FoodAdapter(private val arrayList: ArrayList<Food>) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder> (){

    private var mListener : ItemClickListener? = null
    interface ItemClickListener{
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener: ItemClickListener){
        mListener = listener
    }

    class ViewHolder (item : View, listener: ItemClickListener?) : RecyclerView.ViewHolder(item) {
        val imageFood : ImageView = item.findViewById(R.id.imageFood)
        val txtName : TextView = item.findViewById(R.id.nameFood)
        val price : TextView = item.findViewById<TextView?>(R.id.price)
        val iconButton : ImageButton = item.findViewById(R.id.iconButton)

        init {
            item.setOnClickListener{
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_food,parent,false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageFood.setImageResource(arrayList[position].imageFood)
        holder.txtName.text = arrayList[position].nameFood
        holder.price.text = arrayList[position].price.toString() + "$"
        holder.iconButton.setImageResource(arrayList[position].iconButton)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}