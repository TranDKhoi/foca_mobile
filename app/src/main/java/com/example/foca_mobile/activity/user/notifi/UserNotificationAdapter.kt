package com.example.foca_mobile.activity.user.notifi

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ListNotifyItemBinding
import com.example.foca_mobile.model.Notification
import kotlinx.android.synthetic.main.list_notify_item.view.*

class UserNotificationAdapter(private val notifyList: MutableList<Notification>) :
    RecyclerView.Adapter<UserNotificationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListNotifyItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ListNotifyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = notifyList[position]


        when (currentItem.iconType) {
            "SUCCESS" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_success)
            "ERROR" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_cancel)
            "MONEY" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_cancel)
        }
        holder.binding.root.notifyTitle.text = currentItem.message

        holder.binding.notifyDate.text = DateUtils.getRelativeTimeSpanString(
            currentItem.updatedAt!!.time,
            System.currentTimeMillis(),
            0L,
            DateUtils.FORMAT_ABBREV_ALL
        ) ?: ""

        holder.binding.root.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return notifyList.size
    }
}