package com.example.foca_mobile.activity.user.notifi

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
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


        when (currentItem.order!!.status) {
            "COMPLETED" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_success)
            "PROCESSED" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_success)
            "CANCELLED" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_cancel)
            "PENDING" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_pending)
            "ARRIVED" -> holder.binding.imageNotify.setImageResource(R.drawable.ic_pending)
        }
        when (currentItem.type) {
            "NEW_ORDER" -> holder.binding.root.notifyTitle.text =
                holder.binding.root.resources.getString(R.string.YourOrder)
                    .plus(currentItem.order!!.id).plus(" ")
                    .plus(holder.binding.root.resources.getString(R.string.UArrivedorder))
            "PENDING_ORDER" -> holder.binding.root.notifyTitle.text =
                holder.binding.root.resources.getString(R.string.YourOrder)
                    .plus(currentItem.order!!.id).plus(" ")
                    .plus(holder.binding.root.resources.getString(R.string.UPendingNoti))
            "PROCESSING_ORDER" -> holder.binding.root.notifyTitle.text =
                holder.binding.root.resources.getString(R.string.YourOrder)
                    .plus(currentItem.order!!.id).plus(" ")
                    .plus(holder.binding.root.resources.getString(R.string.UProcessedNoti))
            "CANCELLED_ORDER" -> holder.binding.root.notifyTitle.text =
                holder.binding.root.resources.getString(R.string.YourOrder)
                    .plus(currentItem.order!!.id).plus(" ")
                    .plus(holder.binding.root.resources.getString(R.string.UCancelledNoti))
            "CUSTOMER_CANCELLED_ORDER" -> holder.binding.root.notifyTitle.text =
                holder.binding.root.resources.getString(R.string.YourOrder)
                    .plus(currentItem.order!!.id).plus(" ")
                    .plus(holder.binding.root.resources.getString(R.string.UCancelledNoti))
            "SUCCESS_ORDER" -> holder.binding.root.notifyTitle.text =
                holder.binding.root.resources.getString(R.string.YourOrder)
                    .plus(currentItem.order!!.id).plus(" ")
                    .plus(holder.binding.root.resources.getString(R.string.UCompletedNoti))
        }

        holder.binding.notifyDate.text = DateUtils.getRelativeTimeSpanString(
            currentItem.createdAt!!.time,
            System.currentTimeMillis(),
            0L,
            DateUtils.FORMAT_ABBREV_ALL
        ) ?: ""

        holder.binding.isNew.visibility =
            if (currentItem.isSeen!!) CardView.GONE else CardView.VISIBLE

        holder.binding.root.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return notifyList.size
    }
}