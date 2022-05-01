package com.example.foca_mobile.activity.user.cart_order.adapter.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.google.android.material.card.MaterialCardView
import java.lang.ref.WeakReference

class CartItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val view = WeakReference(itemView)
    private lateinit var btnDelete : MaterialCardView

    var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)?=null

    init {
        view.get()?.let {
            it.setOnClickListener{
                if(view.get()?.scrollX != 0){
                    view.get()?.scrollTo(0,0)
                }
            }

            btnDelete = it.findViewById(R.id.btnDelete)
            btnDelete.setOnClickListener{
                onDeleteClick?.let {
                    onDeleteClick -> onDeleteClick(this)
                }
            }

        }
    }

    fun updateView(){
        view.get()?.scrollTo(0,0)
    }
}