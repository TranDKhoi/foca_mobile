package com.example.foca_mobile.activity.admin.chat.listmess

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.utils.LoginPrefs

class ListConversationAdapter(
    private val messList: ArrayList<Conversation>
) :
    RecyclerView.Adapter<ListConversationAdapter.MyViewHolder>() {

    var onItemClick: ((Conversation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_message_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = messList[position]

        val user = LoginPrefs.getUser()
        val partner = currentItem.members?.find { it.id != user.id }

        Glide.with(holder.itemView.context).load(partner?.photoUrl!!).into(holder.messImage)
        holder.messName.text = partner.fullName
        holder.messLast.text = currentItem.lastMessage?.text ?: ""
        holder.messTime.text = DateUtils.getRelativeTimeSpanString(
            currentItem.lastMessage?.createdAt!!.time,
            System.currentTimeMillis(),
            0L,
            DateUtils.FORMAT_ABBREV_ALL
        )

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return messList.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val messImage: ImageView = itemView.findViewById(R.id.messImage)
        val messName: TextView = itemView.findViewById(R.id.messName)
        val messLast: TextView = itemView.findViewById(R.id.messLast)
        val messTime: TextView = itemView.findViewById(R.id.messTime)
    }
}