package com.example.foca_mobile.activity.user.chat.conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.utils.LoginPrefs

class ConversationAdapter(val context: Context, senderId: Int, roomId: Int ,val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMess = itemView.findViewById<TextView>(R.id.sendItem)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMess = itemView.findViewById<TextView>(R.id.receiveItem)
    }


    val ITEM_RECEIVE = 1;
    val roomId = roomId;
    val senderId = senderId;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.receive_message_item, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.send_message_item, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMess = messageList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            //do stuff for send holder
            val viewHolder = holder as SendViewHolder
            holder.sendMess.text = currentMess.text

        } else {
            //do stuff for receive holder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMess.text = currentMess.text
        }
    }

    override fun getItemCount(): Int {
        return messageList.size;
    }


    override fun getItemViewType(position: Int): Int {

        val currentMess = messageList[position]

        val user = LoginPrefs.getUser()
        if (user.id == currentMess.senderId) {
            return senderId;
        } else {
            return ITEM_RECEIVE;
        }
    }
}