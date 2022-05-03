package com.example.foca_mobile.activity.admin.chat.conversation


import android.content.Context
import android.util.Log
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
    val messageList: ArrayList<Message>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMess = itemView.findViewById<TextView>(R.id.sendItem)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMess = itemView.findViewById<TextView>(R.id.receiveItem)
    }


    val ITEM_RECEIVE = 1;
    val ITEM_SEND = 2;

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
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }
}

//class ConversationAdapter1(val context: Context, val conversation: ArrayList<Message>) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val sendMess = itemView.findViewById<TextView>(R.id.sendItem)
//    }
//
//    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val receiveMess = itemView.findViewById<TextView>(R.id.receiveItem)
//
//    }
//
//
//    val ITEM_RECEIVE = 1;
//    val ITEM_SEND = 2;
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == 1) {
//            val view: View =
//                LayoutInflater.from(context).inflate(R.layout.receive_message_item, parent, false)
//            return ReceiveViewHolder(view)
//        } else {
//            val view: View =
//                LayoutInflater.from(context).inflate(R.layout.send_message_item, parent, false)
//            return SendViewHolder(view)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        val currentMess = conversation[position]
//
//        if (holder.javaClass == SendViewHolder::class.java) {
//            //do stuff for send holder
//            val viewHolder = holder as SendViewHolder
//            holder.sendMess.text = currentMess.message
//
//        } else {
//            //do stuff for receive holder
//            val viewHolder = holder as ReceiveViewHolder
//            holder.receiveMess.text = currentMess.message
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return conversation.size;
//    }
//
//
//    override fun getItemViewType(position: Int): Int {
//
//        val currentMess = conversation[position]
//        val user = LoginPrefs.getUser()
//
//        if (user.id == currentMess.senderId) {
//            return ITEM_SEND;
//        } else {
//            return ITEM_RECEIVE;
//        }
//    }
//}