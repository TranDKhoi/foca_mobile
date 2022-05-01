package com.example.foca_mobile.activity.user.chat.listmess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R

class ListMessageAdapter(
    private val messList: ArrayList<ListMessageClass>
) :
    RecyclerView.Adapter<ListMessageAdapter.MyViewHolder>() {

    var onItemClick: ((ListMessageClass) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_message_item, parent, false);
        return MyViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = messList[position];
        holder.messImage.setImageResource(currentItem.image)
        holder.messName.text = currentItem.name;
        holder.messLast.text = currentItem.lastMess;
        holder.messTime.text = currentItem.lastTime.toString();

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return messList.size;
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val messImage: ImageView = itemView.findViewById(R.id.messImage)
        val messName: TextView = itemView.findViewById(R.id.messName)
        val messLast: TextView = itemView.findViewById(R.id.messLast)
        val messTime: TextView = itemView.findViewById(R.id.messTime)
    }
}