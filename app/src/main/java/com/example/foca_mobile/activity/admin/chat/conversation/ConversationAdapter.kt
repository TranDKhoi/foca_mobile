package com.example.foca_mobile.activity.admin.chat.conversation


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.utils.LoginPrefs

class ConversationAdapter(
    val context: Context,
    private val messageList: ArrayList<Message>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMess: TextView = itemView.findViewById(R.id.sendItem)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMess: TextView = itemView.findViewById(R.id.receiveItem)
    }


    val ITEM_RECEIVE = 1
    val ITEM_SEND = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 1) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.receive_message_item, parent, false)
            ReceiveViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.send_message_item, parent, false)
            SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMess = messageList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            //do stuff for send holder
            holder as SendViewHolder
            holder.sendMess.text = currentMess.text

        } else {
            //do stuff for receive holder
            holder as ReceiveViewHolder
            holder.receiveMess.text = currentMess.text
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    override fun getItemViewType(position: Int): Int {

        val currentMess = messageList[position]

        val user = LoginPrefs.getUser()
        return if (user.id == currentMess.senderId) {
            ITEM_SEND
        } else {
            ITEM_RECEIVE
        }
    }
}